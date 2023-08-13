import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.*

plugins {
	id("org.springframework.boot") version "3.1.2"
	id("io.spring.dependency-management") version "1.1.2"
	id("com.google.protobuf") version "0.8.19"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

protobuf {
	// Configure the protoc executable.
	protoc {
		// Download from the repository.
		artifact = "com.google.protobuf:protoc:3.22.3"
	}

	// Locate the codegen plugins.
	plugins {
		// Locate a plugin with name 'grpc'.
		id("grpc") {
			// Download from the repository.
			artifact = "io.grpc:protoc-gen-grpc-java:1.56.0"
		}
		id("grpckt") {
			artifact = "io.grpc:protoc-gen-grpc-kotlin:1.0.0:jdk7@jar"
		}
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				id("grpc") {}
				id("grpckt")
			}
		}
	}
}

dependencies {
	// armeria
	listOf(
		"armeria-spring-boot3-webflux-starter",
		"armeria-kotlin",
		"armeria-grpc",
		"armeria-grpc-kotlin"
	).forEach {
		implementation("com.linecorp.armeria:${it}:1.24.3")
	}

	// r2dbc
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("io.asyncer:r2dbc-mysql:1.0.2")

	// grpc
	implementation("io.grpc:grpc-kotlin-stub:1.0.0")
	implementation("javax.annotation:javax.annotation-api:1.3.2")

	implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
