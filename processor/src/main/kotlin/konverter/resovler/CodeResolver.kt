package konverter.resovler

import com.squareup.kotlinpoet.FileSpec
import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertPoetInfo
import konverter.helper.filer

object CodeResolver {

    fun generate(meta: KonvertMetaInfo, poet: List<KonvertPoetInfo>) {
        val fileName = "konvert-generated"
        val packageName = meta.packageElement.toString()

        val fileBuilder = FileSpec.builder(packageName, fileName)

        poet.forEach {
            fileBuilder.addFunction(it.funSpec)
            it.imports.forEach { import ->
                fileBuilder.addImport(import.packageName, import.name)
            }
        }

        fileBuilder.build().writeTo(filer)
    }
}