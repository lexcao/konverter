package konverter.domain.kapt

import konverter.Konvert
import konverter.domain.poet.component.ExtensionFunction
import konverter.helper.getAnnotationClassValue
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

data class KonvertMeta(
    override val annotatedClass: TypeElement
) : Meta<Konvert> {

    val toClass = annotatedClass.getAnnotationClassValue<Konvert> { to }

    override val clazz: KClass<Konvert>
        get() = Konvert::class

    lateinit var function: ExtensionFunction
}