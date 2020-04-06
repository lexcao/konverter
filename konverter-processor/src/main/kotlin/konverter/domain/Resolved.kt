package konverter.domain

import konverter.helper.nullable
import javax.lang.model.element.VariableElement

data class Resolved(
    val original: VariableElement,
    val name: String = original.simpleName.toString()
) {
    val annotations = original.annotationMirrors
    val nullable = original.nullable()
    val type = original.asType()
}
