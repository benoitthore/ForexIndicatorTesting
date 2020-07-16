plugins {
    id("application")
    id("java")
    id("kotlin")
}

//application {
//    mainClassName = "com.bugsnag.example.kotlinmp.Main"[
//}

dependencies {
    // kotlin jdk
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}")
    implementation(project(":common"))
    implementation("com.bugsnag:bugsnag:${Versions.bugsnagJvm}")
// https://mvnrepository.com/artifact/junit/junit
    // https://mvnrepository.com/artifact/org.openjfx/javafx
//    implementation("org.openjfx:javafx:14")


    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
    testImplementation("io.mockk:mockk:1.10.0")
    // https://mvnrepository.com/artifact/io.ktor/ktor-server-core


}

