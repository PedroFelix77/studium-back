# Dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copia arquivos de build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Torna o mvnw executável e baixa dependências
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copia código fonte
COPY src ./src

# Build da aplicação
RUN ./mvnw clean package -DskipTests

# Imagem de produção
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copia o JAR
COPY --from=builder /app/target/*.jar app.jar

# Porta (Render usa $PORT)
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["sh", "-c", "java -jar -Dserver.port=${PORT:-8080} app.jar"]