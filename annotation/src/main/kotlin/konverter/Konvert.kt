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
 *  warning: the field name of the original need to match that of converting
 *  but you can use @Konvert.Field for mapping
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Konvert(
    val to: KClass<*>
) {

    /**
     *  Specify the field from converted class to mapping with
     */
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Field(
        val name: String
    )

    /**
     *  Use raw code for converting to
     */
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class Code(
        val expression: String
    )

    /**
     *  Define a custom converter for specified type
     *  @see KonvertBy
     */
    @Target(AnnotationTarget.FIELD)
    @Retention(AnnotationRetention.SOURCE)
    annotation class By(
        val value: KClass<out KonvertBy<*, *>>
    )

    interface KonvertBy<From, To> {
        fun From.konvert(): To
    }
}
