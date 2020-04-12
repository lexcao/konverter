package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.helper.Side
import konverter.helper.typeUtils

object SameTypeHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return typeUtils.isSameType(from.type, to.type)
    }

    override fun handle(context: HandlerContext): KonvertResolvedInfo {
        val (from, to, implicitThis) = context
        return KonvertResolvedInfo(
            expression = "$implicitThis." + if (from.side == Side.FROM) from.fromName else to.toName
        )
    }
}