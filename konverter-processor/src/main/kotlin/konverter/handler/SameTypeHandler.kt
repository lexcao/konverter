package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.helper.typeUtils
import javax.lang.model.element.VariableElement

object SameTypeHandler : KonvertHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return typeUtils.isSameType(from.asType(), to.asType())
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = from.simpleName.toString()
        )
    }
}