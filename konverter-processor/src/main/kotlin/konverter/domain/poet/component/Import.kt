package konverter.domain.poet.component

import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

data class Import(
    val qualifiedName: String
) {
    constructor(clazz: KClass<*>) : this(clazz.qualifiedName!!)
    constructor(element: TypeElement) : this(element.qualifiedName.toString())
}
