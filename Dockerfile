# Build con JDK + Maven Wrapper, runtime solo con JRE para reducir el tamaño final.

FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Resuelve dependencias offline antes de copiar el código fuente para aprovechar la cache de capas.
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -ntp dependency:go-offline

# Copia el código y empaqueta el .jar (los tests se ejecutan en el flujo CI/local previo).
COPY src/ src/
RUN ./mvnw -B -ntp -DskipTests clean package

# Imagen final: solo runtime (sin compilador), pesa ~150 MB.
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copia el .jar empaquetado desde la etapa de build.
COPY --from=build /workspace/target/mascotas-ordenes-0.0.1-SNAPSHOT.jar app.jar

# Ruta donde se monta el Wallet de Oracle ADB (volumen externo, fuera de la imagen).
ENV ORACLE_WALLET_PATH=/app/wallet

EXPOSE 8082
ENTRYPOINT ["java","-jar","/app/app.jar"]
