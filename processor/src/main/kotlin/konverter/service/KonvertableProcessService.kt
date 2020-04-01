package konverter.service

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.asTypeName
import konverter.Konvertable
import konverter.To
import konverter.domain.kapt.KonvertableMeta
import konverter.domain.kapt.Meta
import konverter.domain.kapt.annotation
import konverter.domain.poet.KonvertableWriter
import konverter.domain.poet.Writable
import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Field
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KonvertableProcessService : ProcessService {

    override fun resolveKAPT(element: TypeElement): Meta {
        val meta = KonvertableMeta(element)

        val classes = LinkedList<DataClass>()
        val functions = LinkedList<ExtensionFunction>()
        meta.annotation<Konvertable>().classes.forEach { to ->
            val toClassName = to.name
            val resolvedFields = handleAnnotation(to, meta.fields)

            val dataClass = DataClass(
                name = toClassName,
                packageName = meta.packageName,
                constructorFields = resolvedFields.map { Field(it) }
            )

            classes += dataClass
            functions += ExtensionFunction(
                name = "to$toClassName",
                receiver = element.asType().asTypeName(),
                returns = ClassName.bestGuess(dataClass.qualifiedName),
                statement = resolvedFields.joinToString(", ") {
                    String.format("%s = %s", it.simpleName, it.simpleName)
                }
            )
        }
        return meta.apply {
            this.classes = classes
            this.functions = functions
        }
    }

    private fun handleAnnotation(
        to: To,
        fields: List<VariableElement>
    ): List<VariableElement> {
        val pick = to.pick
        val omit = to.omit
        return fields.run {
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
    }

    override fun resolvePoet(meta: List<Meta>): Writable {
        val filtered = meta.filterIsInstance<KonvertableMeta>()
        val flatClasses = filtered.flatMap { it.classes }
        val flatFunctions = filtered.flatMap { it.functions }

        return KonvertableWriter(
            packageName = meta.first().packageName,
            classes = flatClasses,
            functions = flatFunctions
        )
    }
}