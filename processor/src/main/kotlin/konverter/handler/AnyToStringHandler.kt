package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.helper.isType
import javax.lang.model.element.VariableElement

class AnyToStringHandler(
    override val from: VariableElement,
    override val to: VariableElement
) : AnnotationHandler {

    override fun support(): Boolean {
        return to.asType().isType<String>()
    }

    override fun handle(): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = "${from.simpleName}.toString()"
        )
    }
}