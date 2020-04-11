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

    override fun handle(from: ResolvedField, to: ResolvedField): KonvertResolvedInfo {
        val expression = if (from.side == Side.FROM) {
            "${from.fromName}.toString()"
        } else {
            "${to.toName}.${to.type.castTo()}"
        }
        return KonvertResolvedInfo(
            expression = expression
        )
    }
}