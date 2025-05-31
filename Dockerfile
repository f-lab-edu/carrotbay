FROM openjdk:21
WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT sh -c "java -jar app.jar --spring.profiles.active=\$SPRING_PROFILES_ACTIVE"
