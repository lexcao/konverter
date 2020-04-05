package konverter.domain.poet

import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction

class KonvertableWriter(
    packageName: String,
    classes: List<DataClass>,
    functions: List<ExtensionFunction>
) : Writable {

    override val poets: Collection<KotlinPoet> = listOf(
        ClassPoet(FILE_NAME_FOR_CLASS, packageName, classes),
        FunctionPoet(FILE_NAME_FOR_FUNCTION, packageName, functions)
    )

    companion object {
        private const val FILE_NAME_FOR_CLASS = "konvertable-class-generated"
        private const val FILE_NAME_FOR_FUNCTION = "konvertable-function-generated"
    }
}