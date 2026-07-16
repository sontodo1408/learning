# ===== Stage 1: Build =====
FROM maven:3.9.11-eclipse-temurin-21 AS builder

WORKDIR /app

# Copy pom trước để tận dụng cache
COPY pom.xml .

RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Nếu có các file khác như .mvn hoặc mvnw thì copy thêm
# COPY .mvn .mvn
# COPY mvnw .

# Build
RUN mvn clean package -DskipTests -Dbuild.profile=production

# ===== Stage 2: Run =====
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]