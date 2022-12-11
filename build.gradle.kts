plugins {
    kotlin("jvm") version "1.7.22"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:31.1-jre")
    implementation("org.jetbrains.kotlinx:multik-core:0.2.0")
    implementation("org.jetbrains.kotlinx:multik-default:0.2.0")
    implementation("org.apache.commons:commons-math3:3.6.1")

}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
    }

    wrapper {
        gradleVersion = "7.6"
    }
}
