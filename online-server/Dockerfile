FROM adoptopenjdk/openjdk8:jre8u442-b05-ea-beta-ubuntu-nightly
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
# Set timezone
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
