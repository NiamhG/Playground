plugins {
    kotlin("jvm") version "1.8.0"
    application
}


group = "com.example"
version = "1.0-SNAPSHOT"


repositories {
    mavenCentral()
}

buildscript {
    repositories {
        mavenCentral()
    }

}

dependencies {
    implementation("org.http4k:http4k-core:5.12.0.0")
    implementation("org.http4k:http4k-server-netty:5.12.0.0")
    implementation("org.http4k:http4k-contract:5.12.0.0")
    implementation("org.http4k:http4k-client-apache:5.12.0.0")
    implementation("org.http4k:http4k-format-jackson:5.12.0.0")
    implementation("org.http4k:http4k-multipart:5.12.0.0")
    implementation("dev.forkhandles:result4k:2.4.0.0")
    implementation("org.webjars:swagger-ui:3.52.3")

    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv:2.15.2")
}


tasks.test {
    useJUnitPlatform()
}
