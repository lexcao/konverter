package konverter.handler

import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import konverter.helper.hasAnnotation
import javax.lang.model.element.VariableElement

object KonvertCodeHandler : AnnotationHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return from.hasAnnotation<Konvert.Code>()
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = from.getAnnotation(Konvert.Code::class.java).expression
        )
    }
}