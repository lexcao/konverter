package konverter.processor

import com.squareup.kotlinpoet.FileSpec
import konverter.Konvert
import konverter.domain.KonvertMetaInfo
import konverter.helper.filer
import konverter.helper.findAnnotatedClassElement
import konverter.helper.initTools
import konverter.helper.packetName
import konverter.resovler.CodeResolver
import konverter.resovler.ContractResolver
import konverter.resovler.MetaResolver
import konverter.resovler.PoetResolver
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("konverter.Konvert")
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

        // 1. find and filter elements related to the annotation processed
        val elements = roundEnv.findAnnotatedClassElement<Konvert>()
        if (elements.isEmpty()) {
            return true
        }

        // 2. process type elements annotated by annotation
        doProcess(elements)

        return true
    }

    // extract to another file if necessary
    private fun doProcess(elements: List<TypeElement>) {
        val poetInfo = elements.map {
            // 1. resolve meta data
            val meta = KonvertMetaInfo(it)

            // 2. resolve relevant annotations
            val membersMap = MetaResolver.resolveMembers(meta)

            // 3. resolve rules
            val resolved = ContractResolver.apply(meta, membersMap)

            // 4. resolve poet element
            PoetResolver.resolve(meta, resolved)
        }

        // 5. generate kotlin code
        CodeResolver.generate(elements.first().packetName, poetInfo)
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