package konverter.builder

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import konverter.helper.elementUtils
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

class KotlinBuilder(
    packetName: String,
    fileName: String
) {

    private val _builder: FileSpec.Builder = FileSpec.builder(packetName, fileName)

    fun function(name: String, builder: FunSpec.Builder.() -> Unit) {
        _builder.addFunction(FunSpec.builder(name).apply(builder).build())
    }

    fun dataClass(name: String, builder: TypeSpec.Builder.() -> Unit) {
        _builder.addType(
            TypeSpec.classBuilder(name).addModifiers(KModifier.DATA)
                .apply(builder)
                .build()
        )
    }

    fun import(clazz: KClass<*>) {
        _builder.addImport(clazz.java.packageName, clazz.simpleName.toString())
    }

    fun import(element: TypeElement) {
        _builder.addImport(
            elementUtils.getPackageOf(element).toString(),
            element.simpleName.toString()
        )
    }

    fun build(): FileSpec {
        return _builder.build()
    }
}