package konverter.domain.poet

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import konverter.builder.kotlin
import konverter.domain.KotlinPoet

class KonvertablePoet(
    override val packageName: String,
    private val classes: List<DataClass>
) : KotlinPoet {

    companion object {
        private const val FILE_NAME = "konvertable-generated"
    }

    override val fileName: String
        get() = FILE_NAME

    override fun write(): FileSpec = kotlin(packageName, FILE_NAME) {
        classes.forEach {
            dataClass(it.name) {
                addKdoc(" Auto generated code by @Konvertable")
                primaryConstructor(it.parameters)
                addProperties(it.properties)
            }
        }
    }

    private fun TypeSpec.Builder.primaryConstructor(
        parameters: Iterable<ParameterSpec>
    ) {
        primaryConstructor(
            FunSpec.constructorBuilder()
                .addParameters(parameters)
                .build()
        )
    }
}