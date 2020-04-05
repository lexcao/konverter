package konverter.domain

import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

data class KonvertResolvedInfo(
    val expression: String,
    val importClasses: List<KClass<*>> = emptyList(),
    val importElements: List<TypeElement> = emptyList()
)
