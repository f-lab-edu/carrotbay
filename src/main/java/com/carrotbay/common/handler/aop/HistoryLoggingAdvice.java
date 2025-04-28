package com.carrotbay.common.handler.aop;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

import com.carrotbay.common.utils.JsonUtil;
import com.carrotbay.domain.history.event.HistoryLoggingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
	private static final String SERVICE_SUFFIX = "Service";
	private static final String REPOSITORY_SUFFIX = "Repository";

	private final ApplicationEventPublisher eventPublisher;
	private final HttpServletRequest request;
	private final ApplicationContext context;
	private final ObjectMapper objectMapper;

	@Around("@annotation(com.carrotbay.common.handler.annotation.LogUpdateHistory)")
	public Object logUpdate(ProceedingJoinPoint joinPoint) throws Throwable {
		Optional<Long> optionalEntityId = extractIdFromUrl();
		Optional<JpaRepository<?, Long>> optionalRepository = resolveRepository(joinPoint);

		if (optionalEntityId.isEmpty() || optionalRepository.isEmpty()) {
			log.warn("logUpdate: 필수 정보(entityId 또는 repository)가 존재하지않습니다.");
			return joinPoint.proceed();
		}

		Long entityId = optionalEntityId.get();
		JpaRepository<?, Long> repository = optionalRepository.get();
		String beforeUpdateStr = getJsonEntityById(repository, entityId);

		Object result = joinPoint.proceed();

		String afterUpdateStr = getJsonEntityById(repository, entityId);
		String entityName = getEntityName(repository);

		if (StringUtils.isNoneBlank(beforeUpdateStr, afterUpdateStr)) {
			HistoryLoggingEvent dto = HistoryLoggingEvent.builder()
				.tableName(entityName)
				.operation(UPDATE_SUFFIX)
				.beforeValue(beforeUpdateStr)
				.afterValue(afterUpdateStr)
				.entityId(entityId)
				.build();
			eventPublisher.publishEvent(dto);
		}

		return result;
	}

	public String getJsonEntityById(JpaRepository<?, Long> repository, Long id) {
		String result = repository.findById(id)
			.map(this::convertEntityToJson)
			.orElse(null);
		if (result == null) {
			log.error("getJsonEntityById : 엔티티를 JSON으로 변환하는 중 오류가 발생했습니다.");
		}
		return result;
	}

	public String convertEntityToJson(Object entity) {
		String result = null;
		try {
			String json = objectMapper.writeValueAsString(entity);
			JsonNode rootNode = objectMapper.readTree(json);
			JsonUtil.getRelatedEntitiesToId(rootNode);
			result = objectMapper.writeValueAsString(rootNode);

		} catch (JsonProcessingException e) {
			log.error("convertEntityToJson : 엔티티를 JSON으로 변환하는 중 오류가 발생했습니다.");
		}
		return result;
	}

	public Optional<Long> extractIdFromUrl() {
		try {
			String uri = request.getRequestURI();
			String[] pathSegments = uri.split("/");
			for (String segment : pathSegments) {
				try {
					return Optional.of(Long.parseLong(segment));
				} catch (NumberFormatException ignored) {
				}
			}
		} catch (Exception e) {
			log.error("ID를 추출할 수 없습니다. 올바른 형식의 URL을 사용해주세요.", e);
		}
		return Optional.empty();
	}

	public Optional<JpaRepository<?, Long>> resolveRepository(ProceedingJoinPoint joinPoint) {
		String entityName = joinPoint.getTarget().getClass().getSimpleName().replace(SERVICE_SUFFIX, "");
		String repositoryBeanName = decapitalize(entityName) + REPOSITORY_SUFFIX;

		try {
			return Optional.of((JpaRepository<?, Long>)context.getBean(repositoryBeanName));
		} catch (Exception e) {
			log.error("Repository 빈을 찾을 수 없습니다: " + repositoryBeanName, e);
		}
		return Optional.empty();
	}

	public String decapitalize(String str) {
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	public String getEntityName(JpaRepository<?, Long> repository) {
		ResolvableType resolvableType = ResolvableType.forClass(Repository.class, repository.getClass());
		Class<?> entityClass = resolvableType.getGeneric(0).resolve();
		if (entityClass == null) {
			log.error("HistoryLoggingAdvice : Repository에서 의존하고있는 entity가 없습니다.");
		}
		return entityClass.getSimpleName();
	}
}
