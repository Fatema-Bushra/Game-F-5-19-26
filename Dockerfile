# Stage 1: Build the Java JAR file
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Set up the headless graphical environment and Webswing
FROM openjdk:17-jdk-slim

# Install Webswing dependencies and virtual frame buffer (Xvfb)
RUN apt-get update && apt-get install --no-install-recommends -y \
    wget \
    unzip \
    xvfb \
    libxext6 \
    libxi6 \
    libxtst6 \
    libxrender1 \
    libpangoft2-1.0-0 \
    && rm -rf /var/lib/apt/lists/*

# Download and extract the public Webswing distribution binaries
WORKDIR /opt
RUN wget https://dev.webswing.org/download/webswing-23.1.zip -O webswing.zip \
    && unzip webswing.zip \
    && mv webswing-23.1 webswing \
    && rm webswing.zip

# Move your compiled JAR and property configurations into place
WORKDIR /opt/webswing
COPY --from=build /app/target/*-jar-with-dependencies.jar /opt/webswing/apps/game.jar
COPY webswing.properties /opt/webswing/webswing.properties

# Expose port 8080 for web requests
EXPOSE 8080

# Configure the virtual frame buffer environment variable
ENV DISPLAY=:99

# Set the entry point to launch Xvfb and then start the Webswing server launcher
ENTRYPOINT ["sh", "-c", "Xvfb :99 -screen 0 1024x768x16 & java -jar /opt/webswing/server/webswing-jetty-launcher.jar -j /opt/webswing/jetty.properties"]