Generate converted data class for Kotlin.

# Background
Since server-side developing with Kotlin, there are many sorts of objects such as BO(business object), DTO(data transfer object), EO(entity object) and POJO(plain old java object) to coding.

It would repeat ourselves to write those classes which fields are almost the same from bottom to top, for example, `UserEntity` to `UserDTO` to `UserBO`.

Here is code generation by KAPT.

# Usage
## dependency
```
repositories {
}

dependencies {
    kapt("")
    implements("")
}
```

## @Konvert
```kotlin

```
which generating:
```kotlin

```

## @Konvertable
```kotlin

```
which generating:
```kotlin
```

# Defect
* default value of reference class, Collection, Array and Map
* `@Konvert` on nested class is not supported

# TODO
* [] support for default value of Collection, Array and Map
* [] alternative way for default value of reference
* [] support for nested class
* [] support for generating Java code


Support for using default parameters from constructor
https://youtrack.jetbrains.com/issue/KT-29355
https://github.com/spring-projects/spring-boot/issues/15397

bugs: 
Capitalize class name 
fix same class name
konvert by with companion object