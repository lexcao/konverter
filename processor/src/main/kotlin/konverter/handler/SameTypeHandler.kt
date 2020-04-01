package konverter.handler

import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

object SameTypeHandler : AnnotationHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return from.asType() == to.asType()
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = from.simpleName.toString()
        )
    }
}