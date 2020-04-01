package konverter.handler

import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import konverter.helper.getAnnotationClassValue
import javax.lang.model.element.VariableElement

class KonvertByHandler(
    override val from: VariableElement,
    override val to: VariableElement
) : AnnotationHandler {

    override fun support(): Boolean {
        return from.getAnnotation(Konvert.By::class.java) != null
    }

    override fun handle(): KonvertResolvedInfo {
        val target = from.getAnnotationClassValue<Konvert.By> { value }
        return KonvertResolvedInfo(
            expression = "with(${target.simpleName}) { ${from.simpleName}.konvert() }",
            importElements = listOf(target)
        )
    }
}