package konverter.domain

import javax.lang.model.element.TypeElement

data class CompositeResolvedInfo(
    val fromElement: TypeElement,
    val toElement: TypeElement,
    val fromField: List<Resolved>,
    val toField: List<Resolved>
) {

    val from2To: Map<Resolved, Resolved?>
    val to2From: Map<Resolved, Resolved?>

    init {
        val fromFieldMap = fromField.associateBy { it.name }
        val toFieldMap = toField.associateBy { it.name }
        from2To = fromField.associateWith { toFieldMap[it.name] }
        to2From = toField.associateWith { fromFieldMap[it.name] }
    }
}
