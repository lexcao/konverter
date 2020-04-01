package konverter.domain.poet

import com.squareup.kotlinpoet.FileSpec
import konverter.builder.kotlin
import konverter.domain.poet.component.ExtensionFunction

class FunctionPoet(
    override val fileName: String,
    override val packageName: String,
    private val functions: List<ExtensionFunction>
) : KotlinPoet {

    override fun write(): FileSpec = kotlin(packageName, fileName) {
        functions.forEach {
            import(it.imports)

            function(it.name) {
                addKdoc(" Auto generated code by @Konvert")
                receiver(it.receiver)
                returns(it.returns)
                addStatement("return %T(${it.statement})", it.returns)
            }
        }
    }
}