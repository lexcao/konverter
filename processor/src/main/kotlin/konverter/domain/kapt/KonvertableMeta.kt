package konverter.domain.kapt

import konverter.Konvertable
import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction
import konverter.helper.fields
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

data class KonvertableMeta(
    override val annotatedClass: TypeElement
) : Meta<Konvertable> {

    override val clazz: KClass<Konvertable>
        get() = Konvertable::class

    val fields: List<VariableElement> = annotatedClass.fields

    lateinit var classes: List<DataClass>
    lateinit var functions: List<ExtensionFunction>
}
