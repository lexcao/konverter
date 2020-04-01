package konverter.domain.poet

import com.squareup.kotlinpoet.TypeName

data class ExtensionFunction(
    val name: String,
    val receiver: TypeName,
    val returns: TypeName,
    val statement: String,
    val imports: List<Import> = emptyList()
)