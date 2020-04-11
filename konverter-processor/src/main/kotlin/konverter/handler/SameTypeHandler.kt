package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.helper.typeUtils

object SameTypeHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return typeUtils.isSameType(from.type, to.type)
    }

    override fun handle(from: ResolvedField, to: ResolvedField): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = from.fromName
        )
    }
}