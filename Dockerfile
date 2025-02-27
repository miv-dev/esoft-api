FROM amazoncorretto:22 AS runtime
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*.jar /app/ktor-docker-sample.jar
ENTRYPOINT ["java","-jar","/app/ktor-docker-sample.jar"]