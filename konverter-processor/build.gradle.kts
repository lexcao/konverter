plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("default") {
            groupId = "io.github.lexcao"
            artifactId = "konverter"
            version = "1.0-SNAPSHOT"

            from(components["java"])
        }
    }
}

dependencies {
    implementation(project(":konverter-annotation"))
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