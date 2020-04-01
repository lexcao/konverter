package konverter.domain.poet

import konverter.domain.poet.component.ExtensionFunction

class KonvertWriter(
    packageName: String,
    functions: List<ExtensionFunction>
) : Writable {

    override val poets: Collection<KotlinPoet> = listOf(
        FunctionPoet(
            fileName = FILE_NAME,
            packageName = packageName,
            functions = functions
        )
    )

    companion object {
        private const val FILE_NAME = "konvert-generated"
    }
}