plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "me.guillaumeginer"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("MainKt")
}
