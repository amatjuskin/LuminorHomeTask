plugins {
    id("java")
    id("io.qameta.allure-report") version "2.12.0"
    id("io.qameta.allure") version "2.12.0"
}

group = "org.example"
version = "main"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.codeborne:selenide:7.9.3")
    testImplementation("org.testng:testng:7.11.0")
    testImplementation("io.qameta.allure:allure-testng:2.29.1")
    testImplementation("io.qameta.allure:allure-selenide:2.29.1")
}

tasks.test {
    useTestNG() {
        useDefaultListeners = true
    }

    testLogging {
        events("PASSED", "FAILED", "SKIPPED")
    }

}