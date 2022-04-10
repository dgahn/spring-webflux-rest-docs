import io.gitlab.arturbosch.detekt.Detekt

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.spring") version "1.6.10"
    kotlin("plugin.jpa") version "1.6.10"
    kotlin("plugin.allopen") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.springframework.boot") version "2.6.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("io.gitlab.arturbosch.detekt") version "1.19.0"
    id("org.jmailen.kotlinter") version "3.9.0"
    id("org.asciidoctor.jvm.convert") version "3.3.2"
}

group = "com.sia"
version = "0.1.0"

repositories {
    mavenCentral()
}

allOpen {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.Embeddable")
    annotation("javax.persistence.MappedSuperclass")
}

noArg {
    annotation("javax.persistence.Entity")
    annotation("javax.persistence.MappedSuperclass")
    annotation("javax.persistence.Embeddable")
    annotation("kotlinx.serialization.Serializable")
}

detekt {
    buildUponDefaultConfig = false // preconfigure defaults
    allRules = false // activate all available (even unstable) rules.
    config =
        files("$projectDir/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt
}

kotlinter {
    ignoreFailures = false
    indentSize = 4
    reporters = arrayOf("checkstyle", "plain")
    experimentalRules = false
    disabledRules = emptyArray()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.10")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("io.projectreactor.netty:reactor-netty:1.0.17")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.6.0")
    implementation("io.netty:netty-tcnative-boringssl-static:2.0.51.Final")
    implementation("io.netty:netty-tcnative:2.0.51.Final:osx-x86_64@jar")

    testImplementation("org.springframework.restdocs:spring-restdocs-webtestclient")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.springframework.restdocs:spring-restdocs-asciidoctor")
    testImplementation("io.kotest:kotest-assertions-core:5.2.1")
    testImplementation("io.mockk:mockk:1.12.3")
    testImplementation("com.ninja-squad:springmockk:3.1.1")
}

val snippetsDir by extra {
    file("build/generated-snippets")
}

tasks {
    withType<Test> {
        useJUnitPlatform()
        testLogging {
            exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        }
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
        dependsOn(detekt)
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    withType<Detekt> {
        dependsOn(formatKotlin)
    }
    withType<org.asciidoctor.gradle.jvm.AsciidoctorTask> {
        inputs.dir(snippetsDir)
        dependsOn(test)
        doLast {
            copy {
                from("build/docs/asciidoc/index.html")
                into("src/main/resources/templates")
            }
        }
    }
    bootJar {
        dependsOn(asciidoctor)
        archiveFileName.set("api-server.jar")
        doLast {
            copy {
                from("build/libs/api-server.jar")
                into("docker/server")
            }
        }
    }
}
