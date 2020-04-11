package konverter

import kotlin.reflect.KClass

/**
 *  Usage:
 *
 *  @Konvert(to = UserDTO::Class)
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
 *  the field name to converted to need match that of original
 *  you can use @Konvert.Field for name mapping
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Konvert(
    val to: KClass<*>
) {

    /**
     *  specify the field from converted class to mapping with
     */
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Field(
        val name: String
    )

    /**
     *  define a custom converter for specified type
     *  @see KonvertBy
     */
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class By(
        val value: KClass<out KonvertBy<*, *>>
    )

    interface KonvertBy<From, To> {
        fun From.forward(): To
        fun To.backward(): From
    }
}
