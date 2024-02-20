FROM openjdk:17

ENV JAVA_HOME $JAVA_17_HOME

VOLUME /tmp

ADD target/welcome-service-server.jar /opt/insurance/welcome-service-server.jar

RUN \
  /usr/sbin/groupadd --gid 603 insurance && \
  /usr/sbin/adduser -u 603 --gid 603 --home /opt/insurance insurance

WORKDIR /opt/insurance
USER insurance

COPY app-start.sh /opt/insurance/app-start.sh

# Datasource to postgres docker-compose service
ENV SPRING_DATASOURCE_URL jdbc:postgresql://welcome-service-model:5432/welcome_service
ENV SPRING_DATASOURCE_USERNAME welcome_service_user
ENV SPRING_DATASOURCE_PASSWORD welcome_service_password


ENV JAVA_OPTS "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5314 -Djavax.net.ssl.trustStore=/config/truststore.jks -Djava.security.egd=file:/dev/./urandom -jar"

HEALTHCHECK --interval=5s --timeout=30s --retries=100 CMD curl -f curl -sS http://localhost:27001/actuator/info || exit 1

CMD ["./app-start.sh"]
