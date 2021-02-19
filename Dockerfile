FROM openjdk:11-jre-slim
COPY "build/libs/*.jar" "/app/app.jar"
WORKDIR /app/
ENTRYPOINT ["java","-Djava.net.preferIPv4Stack=true","-jar","app.jar"]
EXPOSE 8080
