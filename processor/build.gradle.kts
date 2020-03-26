plugins {
    kotlin("jvm")
}

dependencies {
    api(project(":annotation"))
    implementation("com.squareup:kotlinpoet:1.5.0")
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}