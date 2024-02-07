plugins {
    id("java")
}

group = "net.logandhillon.controllerlink"
version = "0.1.0-dev"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("uk.co.electronstudio.sdl2gdx:sdl2gdx:1.0.4")
}

tasks.test {
    useJUnitPlatform()
}