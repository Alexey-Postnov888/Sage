plugins {
    kotlin("jvm") version "2.3.21"
    kotlin("plugin.spring") version "2.3.21"
    id("org.springframework.boot") version "4.1.2"
    id("io.spring.dependency-management") version "1.1.7"
}

springBoot {
    mainClass.set("ru.sage.registryservice.RegistryServiceApplicationKt")
}

group = "ru.sage"
version = "1.0.0-SNAPSHOT"
description = "RegistryService"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

val exposedVersion = "1.4.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("org.jetbrains.exposed:exposed-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-jdbc:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-java-time:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-json:${exposedVersion}")

    runtimeOnly("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.jetbrains.exposed:exposed-migration-core:${exposedVersion}")
    implementation("org.jetbrains.exposed:exposed-migration-jdbc:${exposedVersion}")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.2.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.springframework.boot:spring-boot-starter-flyway-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.register<JavaExec>("generateMigrations") {
    group = "exposed"
    description = "Generates Flyway migration from Exposed tables"

    dependsOn(tasks.classes)

    mainClass = "ru.sage.registryservice.data.exposed.generator.MigrationGeneratorKt"
    classpath = sourceSets["main"].runtimeClasspath
}


// fix for CVE-2026-110879
ext["jackson-bom.version"] = "3.1.8"
ext["jackson-2-bom.version"] = "2.21.8"