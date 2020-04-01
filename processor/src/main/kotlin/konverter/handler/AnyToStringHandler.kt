package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.helper.isType
import javax.lang.model.element.VariableElement

object AnyToStringHandler : AnnotationHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return to.asType().isType<String>()
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = "${from.simpleName}.toString()"
        )
    }
}