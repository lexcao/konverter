package konverter.handler

import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

object DefaultHandler : KonvertHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        return true
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = "TODO(\"[${from.asType()}]·cannot·convert·to·[${to.asType()}]\")"
        )
    }
}