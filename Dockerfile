# Coloca este Dockerfile DENTRO de la carpeta Intuit/

# Etapa de construcción (build stage)
# Usamos una imagen con JDK 17 para construir
FROM eclipse-temurin:17-jdk-jammy AS builder

# El WORKDIR es el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia los archivos de configuración de Gradle Wrapper, build y settings
# Estas rutas son relativas al contexto de construcción, que es ./Intuit
COPY gradlew .
COPY gradle/ gradle/
COPY build.gradle .
# Si tienes settings.gradle, descomenta la siguiente línea:
# COPY settings.gradle .


# Opcional: Hacer el wrapper ejecutable (necesario si no lo es por defecto)
RUN chmod +x gradlew

# Descarga las dependencias para aprovechar el cache de Docker
# Ejecutamos la tarea 'dependencies' con --no-daemon (recomendado en Docker)
RUN ./gradlew dependencies --no-daemon

# Copia el código fuente de tu aplicación
# La ruta es relativa al contexto de construcción ./Intuit
COPY src ./src

# Construye la aplicación y empaqueta el JAR
# Usamos 'build' y saltamos los tests con '-x test', también con --no-daemon
RUN ./gradlew build -x test --no-daemon

# Etapa de ejecución (run stage)
# Usamos una imagen más ligera con solo JRE 17 para ejecutar
FROM eclipse-temurin:17-jre-jammy

# Directorio de trabajo dentro del contenedor de ejecución
WORKDIR /app

# Copia el JAR construido desde la etapa 'builder'
# En Gradle, el JAR se encuentra típicamente en build/libs/
COPY --from=builder /app/build/libs/*.jar /app/app.jar
# Si tu JAR tiene un nombre específico diferente a '*.jar', ajústalo aquí

# Expone el puerto que usa tu aplicación (por defecto 8080 para Spring Boot)
EXPOSE 8080

# Define el punto de entrada para ejecutar la aplicación Java
ENTRYPOINT ["java", "-jar", "app.jar"]