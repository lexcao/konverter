package konverter.domain

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import konverter.helper.Side
import konverter.helper.fields
import javax.lang.model.element.TypeElement

data class BuildFunctionInfo(
    val name: String,
    val type: TypeName,
    val fields: List<ResolvedField>
) {
    constructor(element: TypeElement, side: Side) : this(
        name = element.simpleName.toString(),
        type = element.asType().asTypeName(),
        fields = element.fields.map { ResolvedField(it, side) }
    )
}
