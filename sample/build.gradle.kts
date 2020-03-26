plugins {
    kotlin("jvm")
    kotlin("kapt")
}

dependencies {
    kapt(project(":processor"))
    implementation(project(":processor"))
    implementation(kotlin("stdlib-jdk8"))
    implementation("junit:junit:4.12")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}