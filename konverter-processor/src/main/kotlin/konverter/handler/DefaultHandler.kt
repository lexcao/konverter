package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField

object DefaultHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return true
    }

    override fun handle(context: HandlerContext): KonvertResolvedInfo {
        val to = context.to
        return KonvertResolvedInfo(
            expression = "TODO(\"[${to.toName}: ${to.type}·should·be·assigned·implicitly]\")"
        )
    }
}