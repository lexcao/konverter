package konverter.handler

import konverter.domain.ResolvedField

data class HandlerContext(
    val from: ResolvedField,
    val to: ResolvedField,
    val implicitThis: String
)
