FROM openjdk:21-slim
LABEL org.opencontainers.image.source="https://github.com/nancho313/loqui-auth"
LABEL org.opencontainers.image.description="Java Microservice that handles the authentication logic made throughout the loqui system."
COPY boot/target/loqui-auth*.jar app.jar
CMD ["java", "-jar", "/app.jar"]