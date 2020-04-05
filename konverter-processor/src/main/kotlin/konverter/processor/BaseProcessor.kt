package konverter.processor

import konverter.helper.filer
import konverter.helper.initTools
import konverter.service.ProcessService
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

abstract class BaseProcessor(
    private val annotation: KClass<out Annotation>,
    private val service: ProcessService
) : AbstractProcessor() {

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        val elements = roundEnv.getElementsAnnotatedWith(annotation.java)
            .filter { it.kind == ElementKind.CLASS }
            .filterIsInstance<TypeElement>()

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

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        initTools(processingEnv)
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(annotation.qualifiedName!!)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }
}