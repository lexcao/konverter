plugins {
    kotlin("jvm")
    `maven-publish`
}

publishing {
    publications {
        create<MavenPublication>("default") {
            artifactId = "konverter"

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