package konverter.handler

import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import konverter.helper.getAnnotationClassValue
import konverter.helper.hasAnnotation
import javax.lang.model.element.VariableElement

object KonvertByHandler : KonvertHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return from.hasAnnotation<Konvert.By>()
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        val target = from.getAnnotationClassValue<Konvert.By> { value }
        return KonvertResolvedInfo(
            expression = "with(${target.simpleName}) { ${from.simpleName}.konvert() }",
            importElements = listOf(target)
        )
    }
}