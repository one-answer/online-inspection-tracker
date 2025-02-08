FROM adoptopenjdk/openjdk8:jre8u442-b05-ea-beta-ubuntu-nightly
RUN apt-get update && apt-get install -y \
    libglib2.0-0 \
    libnss3 \
    libnspr4 \
    libdbus-1-3 \
    libatk1.0-0 \
    libatk-bridge2.0-0 \
    libcups2 \
    libx11-6 \
    libxcomposite1 \
    libxext6 \
    libxfixes3 \
    libxrandr2 \
    libgbm1 \
    libxcb1 \
    libpango-1.0-0 \
    libasound2 \
    libatspi2.0-0
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
# Set timezone
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
