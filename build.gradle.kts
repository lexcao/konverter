buildscript {
    repositories {
        jcenter()
        mavenCentral()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
    }
}

subprojects {

    group = "io.github.lexcao"
    version = "1.02"

    repositories {
        jcenter()
        mavenCentral()
    }
}
