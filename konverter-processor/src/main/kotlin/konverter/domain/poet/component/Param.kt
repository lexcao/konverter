package konverter.domain.poet.component

import com.squareup.kotlinpoet.TypeName

data class Param(
    val name: String,
    val type: TypeName,
    val expression: String,
    val nullable: Boolean = false
)
