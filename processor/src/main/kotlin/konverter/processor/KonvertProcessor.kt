package konverter.processor

import konverter.Konvert
import konverter.helper.filer
import konverter.helper.findElementsAnnotatedWith
import konverter.helper.initTools
import konverter.service.KonvertProcessService
import konverter.service.ProcessService
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

    private val service: ProcessService<Konvert> = KonvertProcessService()

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        initTools(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        val elements = roundEnv.findElementsAnnotatedWith<Konvert>()
        if (elements.isEmpty()) {
            return true
        }

        val meta = elements.map {
            service.resolveKAPT(it)
        }
        meta.groupBy { it.packageName }.forEach { (_, it) ->
            val writer = service.resolvePoet(it)
            writer.write(filer)
        }

        return true
    }
}