package konverter.handler

import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

class SameTypeHandler(
    override val from: VariableElement,
    override val to: VariableElement
) : AnnotationHandler {

    override fun support(): Boolean {
        return from.asType() == to.asType()
    }

    override fun handle(): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = from.simpleName.toString()
        )
    }
}