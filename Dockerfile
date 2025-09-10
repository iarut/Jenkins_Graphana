#FROM jenkins/jenkins:lts-jdk17
#USER root
#RUN apt-get update \
# && DEBIAN_FRONTEND=noninteractive \
#    apt-get install --no-install-recommends --assume-yes \
#      docker.io \
#
#ENV PATH="/usr/local/bin:${PATH}"
#
#RUN apt-get update && apt-get install -y docker-compose
#
#RUN apt-get update && apt-get install -y \
#    curl \
#    unzip \
#    python3 \
#    python3-pip
#
## Установка docker-compose
#RUN curl -L "https://github.com/docker/compose/releases/download/v2.40.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose \
#    && chmod +x /usr/local/bin/docker-compose
#USER jenkins

FROM jenkins/jenkins:lts-jdk17

# Переключаемся на root для установки пакетов
USER root

# Установка Docker, curl и других зависимостей
RUN apt-get update && DEBIAN_FRONTEND=noninteractive \
    apt-get install -y --no-install-recommends \
        docker.io \
        curl \
        unzip \
        python3 \
        python3-pip \
    && rm -rf /var/lib/apt/lists/*

# Установка docker-compose
RUN curl -L "https://github.com/docker/compose/releases/download/v2.40.0/docker-compose-$(uname -s)-$(uname -m)" \
    -o /usr/local/bin/docker-compose \
    && chmod +x /usr/local/bin/docker-compose

# Добавим /usr/local/bin в PATH, чтобы jenkins видел docker-compose
ENV PATH="/usr/local/bin:${PATH}"

# Разрешаем пользователю jenkins использовать Docker (для запуска контейнеров из пайплайна)
RUN usermod -aG docker jenkins

# Возврат к пользователю Jenkins
USER jenkins

# Опционально: expose port для Jenkins
EXPOSE 8080