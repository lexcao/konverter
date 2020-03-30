package konverter

/**
 *  Usage:
 *
 *  @Konvertable(name = UserDTO)
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
annotation class Konvertable(

    /**
     *  the name of the generated data class
     */
    val name: String,

    /**
     *  the filed to be omitted
     *  empty means nothing omitted
     */
    val omit: Array<String> = [],

    /**
     *  the filed to pick with
     *  empty means full fields picked
     */
    val pick: Array<String> = []
)