package com.carrotbay.common.handler.aop;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.Id;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Hibernate;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import com.carrotbay.config.auth.LoginUser;
import com.carrotbay.domain.history.dto.HistoryRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class HistoryLoggingAdvice {

	private static final String UPDATE_SUFFIX = "update";
	private static final String CONTROLLER_SUFFIX = "Controller";
	private static final String REPOSITORY_SUFFIX = "Repository";

	private final ApplicationEventPublisher eventPublisher;
	private final HttpServletRequest request;
	private final ApplicationContext context;
	private final ObjectMapper objectMapper;

	private final Map<Class<?>, List<Field>> fieldCache = new ConcurrentHashMap<>();
	private final Map<Class<?>, Field> idFieldCache = new ConcurrentHashMap<>();

	@Around("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public Object logUpdate(ProceedingJoinPoint joinPoint) throws Throwable {

		try {
			Long entityId = extractIdFromUrl();
			JpaRepository<?, Long> repository = resolveRepository(joinPoint);
			Long userId = extractUserIdFromArguments(joinPoint);
			String beforeUpdateStr = fetchEntityAsJson(repository, entityId);

			Object result = joinPoint.proceed();

			String afterUpdateStr = fetchEntityAsJson(repository, entityId);
			String entityName = getEntityName(repository);

			if (StringUtils.isNoneBlank(beforeUpdateStr, afterUpdateStr)) {
				HistoryRequestDto.CreateHistoryDto dto = HistoryRequestDto.CreateHistoryDto.builder()
					.tableName(entityName)
					.operation(UPDATE_SUFFIX)
					.beforeValue(beforeUpdateStr)
					.afterValue(afterUpdateStr)
					.entityId(entityId)
					.createBy(userId)
					.build();
				eventPublisher.publishEvent(dto);
			}

			return result;

		} catch (Exception e) {
			log.error("HistoryLoggingAdvice 처리 중 오류 발생", e);
			throw e;
		}
	}

	public String fetchEntityAsJson(JpaRepository<?, Long> repository, Long id) {
		return repository.findById(id)
			.map(this::convertEntityToJson)
			.orElse(null);
	}

	public Long extractIdFromUrl() {
		try {
			String uri = request.getRequestURI();
			String idStr = uri.substring(uri.lastIndexOf("/") + 1);
			return Long.parseLong(idStr);
		} catch (Exception e) {
			log.warn("URL 기반 ID 추출 실패: {}", e.getMessage());
			return null;
		}
	}

	public JpaRepository<?, Long> resolveRepository(ProceedingJoinPoint joinPoint) {
		String entityName = joinPoint.getTarget().getClass().getSimpleName().replace(CONTROLLER_SUFFIX, "");
		String repositoryBeanName = decapitalize(entityName) + REPOSITORY_SUFFIX;

		try {
			return (JpaRepository<?, Long>)context.getBean(repositoryBeanName);
		} catch (Exception e) {
			throw new IllegalStateException("HistoryLoggingAdvice : Repository 빈을 찾을 수 없습니다: " + repositoryBeanName, e);
		}
	}

	public String decapitalize(String str) {
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	public Long extractUserIdFromArguments(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();
		Object[] args = joinPoint.getArgs();
		Parameter[] parameters = method.getParameters();

		for (int i = 0; i < parameters.length; i++) {
			if (parameters[i].isAnnotationPresent(LoginUser.class) && args[i] instanceof Long) {
				return (Long)args[i];
			}
		}
		return null;
	}

	public Map<String, Object> convertEntityToMap(Object entity) {
		Map<String, Object> result = new HashMap<>();
		Object realEntity = Hibernate.unproxy(entity);
		Class<?> entityClass = realEntity.getClass();

		List<Field> fields = fieldCache.computeIfAbsent(entityClass, cls -> {
			Field[] declaredFields = cls.getDeclaredFields();
			for (Field field : declaredFields) {
				field.setAccessible(true);
			}
			return Arrays.asList(declaredFields);
		});

		for (Field field : fields) {
			try {
				Object value = field.get(realEntity);
				if (value instanceof Enum<?>) {
					result.put(field.getName(), ((Enum<?>)value).name());
				} else if (value != null && value.getClass().getPackageName().startsWith("com.carrotbay.domain")) {
					Object unProxiedValue = Hibernate.unproxy(value);
					Class<?> valueClass = unProxiedValue.getClass();

					Field idField = idFieldCache.computeIfAbsent(valueClass, cls -> {
						for (Field declaredField : cls.getDeclaredFields()) {
							if (declaredField.isAnnotationPresent(Id.class)) {
								declaredField.setAccessible(true);
								return declaredField;
							}
						}
						return null;
					});

					if (idField != null) {
						String fieldName = field.getName() + "_id";
						result.put(fieldName, idField.get(unProxiedValue));
					}
				} else {
					result.put(field.getName(), String.valueOf(value));
				}
			} catch (Exception e) {
				log.warn("HistoryLoggingAdvice : 엔티티 필드 변환 중 오류: {}", e.getMessage());
			}
		}
		return result;
	}

	public String convertEntityToJson(Object entity) {
		try {
			return objectMapper.writeValueAsString(convertEntityToMap(entity));
		} catch (JsonProcessingException e) {
			throw new RuntimeException("HistoryLoggingAdvice : 엔티티 JSON 변환 실패", e);
		}
	}

	public String getEntityName(JpaRepository<?, Long> repository) {
		ResolvableType resolvableType = ResolvableType.forClass(Repository.class, repository.getClass());
		Class<?> entityClass = resolvableType.getGeneric(0).resolve();

		if (entityClass == null) {
			throw new IllegalStateException("HistoryLoggingAdvice : Repository에서 의존하고있는 entity가 없습니다.");
		}

		return entityClass.getSimpleName();
	}
}
