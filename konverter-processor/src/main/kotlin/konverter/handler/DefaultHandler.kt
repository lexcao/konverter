package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField

object DefaultHandler : KonvertHandler {

    override fun support(from: ResolvedField, to: ResolvedField): Boolean {
        return true
    }

    override fun handle(from: ResolvedField, to: ResolvedField): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            expression = "TODO(\"[${from.type}]·cannot·convert·to·[${to.type}]\")"
        )
    }
}