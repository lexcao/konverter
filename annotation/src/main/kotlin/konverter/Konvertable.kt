package konverter

/**
 *  Usage:
 *
 *  @Konvertable(
 *      To(name = "UserDTO")
 *  )
 *  data class UserEntity(
 *      val id: String,
 *      val name: String
 *  )
 *
 *  Then generate:
 *
 *  data class UserDTO (
 *      val id: String,
 *      val name: String
 *  )
 *
 *  fun UserEntity.toUserDTO() = UserDTO(
 *      id = id,
 *      name = name
 *  )
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Konvertable(vararg val classes: To)
