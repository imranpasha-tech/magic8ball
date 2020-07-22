FROM openjdk:8-jdk-slim

COPY resources/@project.build.finalName@.jar /application/lib/app.jar
COPY resources/newrelic-agent-@newrelic.version@.jar /application/lib/newrelic.jar
COPY newrelic.yml /application/lib/newrelic.yml

EXPOSE 8080

CMD ["java","-javaagent:/application/lib/newrelic.jar","-jar","/application/lib/app.jar"]