package konverter.domain.poet

import com.squareup.kotlinpoet.FileSpec
import konverter.domain.KotlinPoet
import konverter.helper.kotlin

class KonvertPoet(
    override val packageName: String,
    private val functions: List<ExtensionFunction>
) : KotlinPoet {

    companion object {
        private const val FILE_NAME = "konvert-generated"
    }

    override val fileName: String
        get() = FILE_NAME

    override fun write(): FileSpec = kotlin(packageName, FILE_NAME) {
        functions.forEach {
            function(it.name) {
                addKdoc(" Auto generated code by @Konvert")
                receiver(it.receiver)
                returns(it.returns)
                addStatement("return %T(${it.statement})", it.returns)
            }
        }
    }
}