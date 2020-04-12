[![JitPack](https://jitpack.io/v/lexcao/konverter.svg)](https://jitpack.io/#lexcao/konverter)

Code generation for converter between data class by KAPT.

See the [blog](https://lexcao.github.io) for more detail.

# Usage
## dependency
```
// for build.gradle.kts
repositories {
    maven("https://jitpack.io")
}

dependencies {
    kapt("com.github.lexcao:konverter:master-SNAPSHOT")
    implementation("com.github.lexcao:konverter-annotation:master-SNAPSHOT")
}

// for build.gradle
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    kapt 'com.github.lexcao:konverter:master-SNAPSHOT'
    implementation 'com.github.lexcao:konverter-annotation:master-SNAPSHOT'
}
```

## example
```
@Konvertable(
    To(name = "LoginDTO", pick = ["username", "password"]),
    To(name = "UserListDTO", omit = ["password"])
)
@Konvert(to = UserVO::class)
data class UserEntity(
    val id: Long,
    @Konvert.Field("name")
    val username: String,
    val password: String,
    @Konvert.By(GenderEnumConverter::class)
    val gender: Int
)
```

## then generate
### for @Konvertable
```
/**
 *  Auto generated code by @Konvertable
 */
data class LoginDTO(
  val username: String,
  val password: String
)

/**
 *  Auto generated code by @Konvertable
 */
data class UserListDTO(
  val id: Long,
  val username: String,
  val gender: Int
)

/**
 *  Auto generated code by @Konvert
 */
fun UserEntity.toLoginDTO(username: String = this@toLoginDTO.username, password: String =
    this@toLoginDTO.password): LoginDTO = LoginDTO(username=username,password=password)

/**
 *  Auto generated code by @Konvert
 */
fun LoginDTO.toUserEntity(
  id: Long = 0L,
  username: String = this@toUserEntity.username,
  password: String = this@toUserEntity.password,
  gender: Int = 0
): UserEntity = UserEntity(id=id,username=username,password=password,gender=gender)

/**
 *  Auto generated code by @Konvert
 */
fun UserEntity.toUserListDTO(
  id: Long = this@toUserListDTO.id,
  username: String = this@toUserListDTO.username,
  gender: Int = this@toUserListDTO.gender
): UserListDTO = UserListDTO(id=id,username=username,gender=gender)

/**
 *  Auto generated code by @Konvert
 */
fun UserListDTO.toUserEntity(
  id: Long = this@toUserEntity.id,
  username: String = this@toUserEntity.username,
  password: String = "",
  gender: Int = this@toUserEntity.gender
): UserEntity = UserEntity(id=id,username=username,password=password,gender=gender)

/**
 *  Auto generated code by @Konvert
 */
fun UserEntity.toRegisterDTO(
  username: String = this@toRegisterDTO.username,
  password: String = this@toRegisterDTO.password,
  gender: Int = this@toRegisterDTO.gender
): RegisterDTO = RegisterDTO(username=username,password=password,gender=gender)

/**
 *  Auto generated code by @Konvert
 */
fun RegisterDTO.toUserEntity(
  id: Long = 0L,
  username: String = this@toUserEntity.username,
  password: String = this@toUserEntity.password,
  gender: Int = this@toUserEntity.gender
): UserEntity = UserEntity(id=id,username=username,password=password,gender=gender)
```

### for @Konvert
```
// the class to convert to 
data class UserVO(
    val id: String,
    val name: String,
    val gender: GenderEnum
)

enum class GenderEnum {
    MALE, FEMALE;

    companion object
}

object GenderEnumConverter : Konvert.KonvertBy<Int, GenderEnum> {
    override fun Int.forward(): GenderEnum {
        return GenderEnum.values()[this]
    }

    override fun GenderEnum.backward(): Int {
        return this.ordinal
    }
}

// then generate
/**
 *  Auto generated code by @Konvert
 */
fun UserEntity.toUserVO(
  id: String = this@toUserVO.id.toString(),
  name: String = this@toUserVO.username,
  gender: GenderEnum = with(GenderEnumConverter) { this@toUserVO.gender.forward() }
): UserVO = UserVO(id=id,name=name,gender=gender)

/**
 *  Auto generated code by @Konvert
 */
fun UserVO.toUserEntity(
  id: Long = this@toUserEntity.id.toLong(),
  username: String = this@toUserEntity.name,
  password: String = "",
  gender: Int = with(GenderEnumConverter) { this@toUserEntity.gender.backward() }
): UserEntity = UserEntity(id=id,username=username,password=password,gender=gender)
```

# Notes
the rules of conversion
* If converting to String type, it will use `toString()` while the type is not match
* If converting to primitive type, it will use default value of primitive type when missing
* If converting to nullable type, it will use `null` for default value when missing
* It will need be assigned explicitly when converting to reference type (excepting String) or unknown type

# Next
* code optimization and test case
* support the default value of object and collection
* support nested class
* support for Java
* fix bugs
* support for using the default value of parameters on constructor or fields on class from original when missing(from now, Kotlin KAPT is only support for [default value of fields](https://youtrack.jetbrains.com/issue/KT-30164), but not for [parameters](https://youtrack.jetbrains.com/issue/KT-29355))
