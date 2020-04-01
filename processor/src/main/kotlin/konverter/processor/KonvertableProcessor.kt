package konverter.processor

import konverter.Konvertable
import konverter.helper.filer
import konverter.helper.findElementsAnnotatedWith
import konverter.service.KonvertableProcessService
import konverter.service.ProcessService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("konverter.Konvertable")
class KonvertableProcessor : AbstractProcessor() {

    private val service: ProcessService<Konvertable> = KonvertableProcessService()

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        val elements = roundEnv.findElementsAnnotatedWith<Konvertable>()

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