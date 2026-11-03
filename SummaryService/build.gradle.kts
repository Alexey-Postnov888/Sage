plugins {
	java
	id("org.springframework.boot") version "4.1.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.sage"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// Web (REST API)
	implementation("org.springframework.boot:spring-boot-starter-webmvc")

	// Валидация (@Size, @NotNull и т.д.)
	implementation("org.springframework.boot:spring-boot-starter-validation")

	// SDK для работы с Yandex Cloud (OpenAI compatible)
	implementation("com.openai:openai-java:2.5.0")

	// Тесты
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}