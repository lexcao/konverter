package konverter.domain

data class CompositeResolvedInfo(
    val from: BuildFunctionInfo,
    val to: BuildFunctionInfo

) {

    val from2To: Map<ResolvedField, ResolvedField?>
    val to2From: Map<ResolvedField, ResolvedField?>

    init {
        val fromFieldMap = from.fields.associateBy { it.fromName }
        val toFieldMap = to.fields.associateBy { it.toName }
        from2To = from.fields.associateWith { toFieldMap[it.toName] }
        to2From = to.fields.associateWith { fromFieldMap[it.fromName] }
    }
}
