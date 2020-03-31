package konverter.processor

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import konverter.Konvertable
import konverter.domain.poet.DataClass
import konverter.domain.poet.ExtensionFunction
import konverter.domain.poet.Field
import konverter.domain.poet.KonvertPoet
import konverter.domain.poet.KonvertablePoet
import konverter.helper.elementUtils
import konverter.helper.filer
import java.lang.String
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("konverter.Konvertable")
class KonvertableProcessor : AbstractProcessor() {

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        // 1. find and filter elements related to the annotation processed
        val elements = roundEnv.getElementsAnnotatedWith(annotations.first())
            .filter { it.kind == ElementKind.CLASS }
            .filterIsInstance<TypeElement>()
        if (elements.isEmpty()) {
            return true
        }

        val packageName = elementUtils.getPackageOf(elements.first()).toString()

        val poetInfo = elements.flatMap { element ->

            val originMembers = element.enclosedElements
                .filter { it.kind == ElementKind.FIELD }
                .filterIsInstance<VariableElement>()

            val konvertable = element.getAnnotation(Konvertable::class.java)
            konvertable.classes.map { to ->
                val pick = to.pick
                val omit = to.omit
                val resolvedMembers = originMembers.run {
                    var list = this
                    if (pick.isNotEmpty()) {
                        list = list.filter { it.simpleName.toString() in pick }
                    }
                    if (omit.isNotEmpty()) {
                        list = list.filterNot { it.simpleName.toString() in omit }
                    }
                    // TODO add friendly comment for confusion omit and pick
                    list
                }

                val toClassName = to.name
                val dataClass = DataClass(
                    name = toClassName,
                    constructorFields = resolvedMembers.map { Field(it) }
                )

                val functions = ExtensionFunction(
                    name = "to$toClassName",
                    receiver = element.asType().asTypeName(),
                    returns = ClassName.bestGuess("$packageName.$toClassName"),
                    statement = resolvedMembers.joinToString(", ") {
                        String.format("%s = %s", it.simpleName, it.simpleName)
                    }
                )

                dataClass to functions
            }
        }

        val classes = poetInfo.map { it.first }
        val functions = poetInfo.map { it.second }

        KonvertablePoet(packageName, classes).write().writeTo(filer)
        KonvertPoet(packageName, functions).write().writeTo(filer)

        return true
    }
}