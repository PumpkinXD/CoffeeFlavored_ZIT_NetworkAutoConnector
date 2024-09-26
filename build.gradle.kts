plugins {
    id("java")
    application
}

group = "io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")
    implementation("org.apache.commons:commons-lang3:3.17.0")
    implementation("commons-codec:commons-codec:1.17.1")
    implementation("org.apache.commons:commons-crypto:1.1.0")
    implementation("com.fasterxml.jackson.core:jackson-core:2.17.2")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("org.jline:jline:3.26.3")



    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector.Main")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "io.github.pumpkinxd.CoffeeFlavored_ZIT_NetworkAutoConnector.Main"
    }
//    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
