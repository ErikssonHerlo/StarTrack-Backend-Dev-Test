# Etapa 1: Compilación
FROM gradle:7.6.0-jdk11 AS builder

WORKDIR /home/gradle/project

# Copiamos los archivos necesarios para la compilación
COPY gradle/ gradle/
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle.properties .
COPY src/ src/

# Ejecutamos la compilación; se generará un fat jar en build/libs
RUN ./gradlew clean build --no-daemon

# Etapa 2: Ejecución
FROM openjdk:11-jre-slim

WORKDIR /app

# Copiamos el jar generado en la etapa de compilación
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Exponemos el puerto en el que corre la aplicación
EXPOSE 8080

# Comando para iniciar la aplicación
CMD ["java", "-jar", "app.jar"]
