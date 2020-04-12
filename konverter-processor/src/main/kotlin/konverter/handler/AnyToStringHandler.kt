package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.helper.Side
import konverter.helper.castTo
import konverter.helper.isType

object AnyToStringHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return to.type.isType<String>() || from.type.isType<String>()
    }

    override fun handle(context: HandlerContext): KonvertResolvedInfo {
        val (from, to, implicitThis) = context

        val expression = if (from.side == Side.FROM) {
            "$implicitThis.${from.fromName}.toString()"
        } else {
            "$implicitThis.${to.toName}.${to.type.castTo()}"
        }
        return KonvertResolvedInfo(
            expression = expression
        )
    }
}