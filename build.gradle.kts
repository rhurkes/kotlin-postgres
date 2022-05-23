import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val ktorVersion = "2.0.1"
val coroutinesVersion = "1.6.1"

plugins {
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.serialization") version "1.6.21"
    application
}

application {
    mainClass.set("ApplicationKt")
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
    }

    dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
        implementation("io.github.microutils:kotlin-logging:2.1.21")
        implementation("ch.qos.logback:logback-classic:1.2.11")
        implementation("ch.qos.logback:logback-core:1.2.11")
        implementation("org.postgresql:postgresql:42.3.4")
        implementation("com.zaxxer:HikariCP:5.0.1")
    }

    tasks {
        withType<KotlinCompile> {
            kotlinOptions.jvmTarget = "17"
            kotlinOptions.allWarningsAsErrors = true
        }
    }
}