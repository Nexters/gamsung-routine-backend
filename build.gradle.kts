import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version Versions.springBootVersion
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version Versions.kotlin
    kotlin("plugin.spring") version Versions.kotlin
    id("org.asciidoctor.jvm.convert") version Versions.asciiDoctorConvertPlugin
    id("com.google.cloud.tools.jib") version Versions.jibVersion
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

group = "gamsung"
version = "0.0.1-SNAPSHOT"

apply(plugin = "org.springframework.boot")
apply(plugin = "io.spring.dependency-management")

apply(plugin = "kotlin")
apply(plugin = "kotlin-kapt")
apply(plugin = "kotlin-spring")

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${Versions.springCloudVersion}")
    }
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("io.jsonwebtoken:jjwt-api:0.11.2")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.2")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.2")
    implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

    implementation("io.springfox:springfox-swagger2:2.9.2")
    implementation("io.springfox:springfox-swagger-ui:2.9.2")
    // Mongo DB
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "junit", module = "junit")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-params")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("io.mockk:mockk:1.12.0")
    testImplementation("de.flapdoodle.embed:de.flapdoodle.embed.mongo:3.0.0")
}

val snippetsDir by extra { file("build/generated-snippets") }

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    outputs.dir(snippetsDir)
}

tasks {
    asciidoctor {
        inputs.dir(snippetsDir)
        dependsOn(test)

        doFirst {
            delete { file(snippetsDir) }
        }
    }

    val copyHTML = register<Copy>("copyHTML") {
        dependsOn(asciidoctor)
        from("build/docs/asciidoc")
        into("src/main/resources/static/docs")
    }

    build {
        dependsOn(copyHTML)
    }

    bootJar {
        dependsOn(asciidoctor)
        from ("build/docs/asciidoc") {
            into("BOOT-INF/classes/static/docs")
        }
        enabled = true
        archiveFileName.set("app.jar")
    }
}
