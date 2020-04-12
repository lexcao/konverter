package konverter.builder

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import konverter.domain.poet.component.Import

class KotlinBuilder(
    packetName: String,
    fileName: String
) {

    private val _builder: FileSpec.Builder = FileSpec.builder(packetName, fileName)

    fun function(name: String, builder: FunSpec.Builder.() -> Unit) {
        _builder.addFunction(FunSpec.builder(name).apply(builder).build())
    }

    fun FunSpec.Builder.param(name: String, type: TypeName, builder: ParameterSpec.Builder.() -> Unit) {
        addParameter(ParameterSpec.builder(name, type).apply(builder).build())
    }

    fun dataClass(name: String, builder: TypeSpec.Builder.() -> Unit) {
        _builder.addType(
            TypeSpec.classBuilder(name).addModifiers(KModifier.DATA)
                .apply(builder)
                .build()
        )
    }

    fun import(imports: Iterable<Import>) {
        _builder.addImport("", imports.map(Import::qualifiedName))
    }

    fun build(): FileSpec {
        return _builder.build()
    }
}