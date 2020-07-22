FROM maven:3-jdk-8-slim
WORKDIR /opt/@project.artifactId@

COPY resources .

WORKDIR /opt/@project.artifactId@/@project.artifactId@

RUN mvn dependency:go-offline

ENV SERVICE_URL https://host.docker.internal

ENV SERVICE_VERSION @project.version@

ENV SERVICE_PORT 8080

ENV GIT_COMMIT_ID @git.commit.id.abbrev@