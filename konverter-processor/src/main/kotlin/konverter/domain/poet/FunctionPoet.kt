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
        functions.withEach {
            import(imports)

            function(name) {
                addKdoc(" Auto generated code by @Konvert")
                params.forEach {
                    param(name = it.name, type = it.type) {
                        if (it.hasDefaultValue) {
                            defaultValue(it.expression)
                        }
                    }
                }
                receiver(receiver)
                returns(returns)
                addStatement("return %T(${statement})", returns)
            }
        }
    }

    private inline fun <T> Iterable<T>.withEach(action: T.() -> Unit) {
        for (element in this) action(element)
    }

    fun test() {

        A("test").copy()
    }

    data class A(val name: String)
}