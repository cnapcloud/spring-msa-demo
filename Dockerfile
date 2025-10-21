FROM openjdk:17-slim
RUN addgroup --system spring &&  adduser --system spring && adduser spring spring 
# USER spring:spring

ADD docker-entrypoint.sh /
COPY project-service/build/libs /app/
COPY orchestrator-service/build/libs /app/
RUN chmod +x ./docker-entrypoint.sh
ENTRYPOINT ["/bin/sh","-c","exec /docker-entrypoint.sh"]
