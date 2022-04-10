FROM adoptopenjdk/openjdk11

RUN mkdir /server

COPY server/api-server.jar /server

CMD ["java", "-jar", "/server/api-server.jar"]