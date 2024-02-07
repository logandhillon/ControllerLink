import org.lwjgl.Lwjgl
import org.lwjgl.lwjgl
import org.lwjgl.sonatype

plugins {
    id("java")
    id("org.lwjgl.plugin") version "0.0.34"
}

group = "net.logandhillon.controllerlink"
version = "0.1.0-dev"

repositories {
    mavenCentral()
    sonatype()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("org.lwjgl:lwjgl:3.3.3")
    lwjgl {
        implementation(Lwjgl.Module.glfw)
    }
}

tasks.test {
    useJUnitPlatform()
}