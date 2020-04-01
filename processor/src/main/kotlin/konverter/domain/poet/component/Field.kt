package konverter.domain.poet.component

import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asTypeName
import konverter.helper.isType
import javax.lang.model.element.VariableElement

data class Field(
    val name: String,
    val type: TypeName
) {
    constructor(it: VariableElement) : this(
        name = it.simpleName.toString(),
        type = if (it.asType().isType<String>()) String::class.asTypeName()
        else it.asType().asTypeName()
    )
}
