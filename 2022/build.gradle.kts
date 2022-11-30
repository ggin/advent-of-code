plugins {
    kotlin("jvm") version "1.7.10"
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
