FROM alpine:latest as build
RUN apk update
RUN apk add openjdk17
COPY . .
RUN chmod +x ./gradlew
RUN ./gradlew bootJar --no-daemon

FROM amazoncorretto:17-alpine
EXPOSE 8080
COPY --from=build ./build/libs/ExamenMercado-1.0-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]