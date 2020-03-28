package konverter.domain

import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

data class KonvertResolvedInfo(
    val origin: VariableElement?,
    val name: String,
    val imports: Set<KClass<*>> = emptySet()
)
