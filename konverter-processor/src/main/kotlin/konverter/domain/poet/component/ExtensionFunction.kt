package konverter.domain.poet.component

import com.squareup.kotlinpoet.TypeName

data class ExtensionFunction(
    val name: String,
    val receiver: TypeName,
    val returns: TypeName,
    val statement: String,
    val params: List<Param>,
    val imports: List<Import> = emptyList()
)