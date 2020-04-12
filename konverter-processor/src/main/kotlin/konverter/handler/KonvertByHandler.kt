package konverter.handler

import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.helper.Side
import konverter.helper.isType

object KonvertByHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return from.annotations.any { it.annotationType.isType<Konvert.By>() } ||
                to.annotations.any { it.annotationType.isType<Konvert.By>() }

    }

    override fun handle(context: HandlerContext): KonvertResolvedInfo {
        val (from, to, implicitThis) = context
        val (real, converter) = if (to.side == Side.TO) {
            from to "forward()"
        } else {
            to to "backward()"
        }
        val target = real.convertedBy!!
        return KonvertResolvedInfo(
            expression = "with(${target.simpleName}) { $implicitThis.${from.fromName}.$converter }",
            importElements = listOf(target)
        )
    }
}