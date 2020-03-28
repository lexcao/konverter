package konverter.processor

import com.squareup.kotlinpoet.FileSpec
import konverter.annotation.Konvert
import konverter.domain.KonvertMetaInfo
import konverter.helper.filer
import konverter.helper.initTools
import konverter.resovler.MetaResolver
import konverter.resovler.PoetResolver
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("konverter.annotation.Konvert")
class KonvertProcessor : AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        initTools(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        roundEnv.getElementsAnnotatedWith(Konvert::class.java)
            .filter { it.kind == ElementKind.CLASS }
            .filterIsInstance<TypeElement>()
            .map(MetaResolver::resolveKonvert)
            .let(::process)

        return true
    }

    private fun process(them: List<KonvertMetaInfo>) {
        if (them.isEmpty()) return
        val fileName = "konvert-generated"
        val packageName = them.first().packageElement.toString()

        val fileBuilder = FileSpec.builder(packageName, fileName)

        them.map(PoetResolver::buildKonvertFunction).forEach {
            fileBuilder.addFunction(it.funSpec)
            it.imports.forEach { import ->
                fileBuilder.addImport(import.packageName, import.name)
            }
        }

        fileBuilder.build().writeTo(filer)
    }
}