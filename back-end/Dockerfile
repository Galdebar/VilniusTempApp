
 
FROM openjdk:11-jre-slim
COPY src /usr/local/lib/app/src
COPY  /target/vilnius-temp-0.0.1-SNAPSHOT.jar /usr/local/lib/app.jar
CMD [ "sh", "-c", "java -Dserver.port=$PORT -Xmx300m -Xss512k -XX:CICompilerCount=2 -Dfile.encoding=UTF-8 -XX:+UseContainerSupport -Djava.security.egd=file:/dev/./urandom -jar /usr/local/lib/app.jar" ]



