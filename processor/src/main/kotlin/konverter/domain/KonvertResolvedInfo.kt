package konverter.domain

import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

data class KonvertResolvedInfo(
    val origin: VariableElement?,
    val expression: String,
    val importClasses: List<KClass<*>> = emptyList(),
    val importElements: List<TypeElement> = emptyList()
)
