package konverter.annotation

import kotlin.reflect.KClass

/**
 *  Usage:
 *
 *  @Konvert(to: UserDTO::Class)
 *  data class UserEntity(
 *      val id: String,
 *      val name: String
 *  )
 *
 *  Then generate:
 *
 *  fun UserEntity.toUserDTO() = UserDTO(
 *      id = id,
 *      name = name
 *  )
 *
 *  warning: the field name of the original need to match that of converting
 *  but you can use @KonvertField for mapping
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Konvert(
    val to: KClass<*>
)
