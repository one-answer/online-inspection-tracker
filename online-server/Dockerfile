FROM debian:12
RUN apt install -y \
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
    libatspi2.0-0 \
    libxdamage1 \
    libxkbcommon0 \
    libcairo2 \
    libx11-xcb1 \
    libxcursor1 \
    libxi6 \
    libgtk-3-0 \
    libpangocairo-1.0-0 \
    libcairo-gobject2 \
    fonts-noto-cjk \
    libgdk-pixbuf2.0-0

VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} /app.jar
# Set timezone
RUN echo "Asia/Shanghai" > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
