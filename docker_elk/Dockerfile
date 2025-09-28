FROM docker.elastic.co/beats/filebeat:7.5.1

# Копируем конфигурационный файл
COPY filebeat.yml /usr/share/filebeat/filebeat.yml

# Устанавливаем правильные права
USER root
RUN chown root:filebeat /usr/share/filebeat/filebeat.yml && \
    chmod 644 /usr/share/filebeat/filebeat.yml

# Переключаемся на пользователя filebeat
USER filebeat