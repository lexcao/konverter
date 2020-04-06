package konverter.domain.kapt

import konverter.Konvert
import konverter.domain.poet.component.ExtensionFunction
import konverter.helper.getAnnotationClassValue
import javax.lang.model.element.TypeElement

data class KonvertMeta(
    override val annotatedClass: TypeElement
) : Meta {

    val toClass = annotatedClass.getAnnotationClassValue<Konvert> { to }

    lateinit var functions: List<ExtensionFunction>
}