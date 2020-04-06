package konverter.domain.kapt

import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction
import konverter.helper.fields
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

data class KonvertableMeta(
    override val annotatedClass: TypeElement,
    val classes: List<DataClass>,
    val functions: List<ExtensionFunction>
) : Meta {
    val fields: List<VariableElement> = annotatedClass.fields
}
