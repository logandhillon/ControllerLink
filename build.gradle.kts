plugins {
    id("java")
}

group = "net.logandhillon.controllerlink"
version = "0.1.0-dev"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("us.ihmc:jinput:2.0.6-ihmc2")
    implementation("net.java.jinput:jinput-platform:2.0.7")
}

tasks.test {
    useJUnitPlatform()
}