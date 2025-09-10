FROM jenkins/jenkins:lts-jdk17
USER root
RUN apt-get update \
 && DEBIAN_FRONTEND=noninteractive \
    apt-get install --no-install-recommends --assume-yes \
      docker.io \

ENV PATH="/usr/local/bin:${PATH}"

RUN apt-get update && apt-get install -y docker-compose

RUN apt-get update && apt-get install -y \
    curl \
    unzip \
    python3 \
    python3-pip

# Установка docker-compose
RUN curl -L "https://github.com/docker/compose/releases/download/v2.40.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
    && chmod +x /usr/local/bin/docker-compose
USER jenkins