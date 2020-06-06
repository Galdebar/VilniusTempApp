# Docker multi-stage build
 
# 1. Building the App with Maven
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package
 
 
# 2. Just using the build artifact and then removing the build-container
FROM openjdk:11-jdk
 
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/vilnius-temp-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
EXPOSE 8080:8080
CMD ["java", "-Dserver.port=$PORT -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom", "-jar","/usr/local/lib/app.jar"]


