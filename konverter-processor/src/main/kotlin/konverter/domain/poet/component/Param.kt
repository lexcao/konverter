package konverter.domain.poet.component

import com.squareup.kotlinpoet.TypeName
import konverter.helper.defaultValueOfObject

data class Param(
    val name: String,
    val type: TypeName,
    val expression: String
) {

    // not support default value for reference object
    val hasDefaultValue: Boolean = expression != defaultValueOfObject
}
