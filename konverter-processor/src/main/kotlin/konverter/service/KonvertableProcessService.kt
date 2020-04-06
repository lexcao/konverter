package konverter.service

import com.squareup.kotlinpoet.asTypeName
import konverter.Konvertable
import konverter.To
import konverter.domain.kapt.KonvertableMeta
import konverter.domain.kapt.Meta
import konverter.domain.poet.KonvertableWriter
import konverter.domain.poet.Writable
import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Field
import konverter.helper.annotation
import konverter.helper.asTypeName
import konverter.helper.fields
import konverter.helper.packetName
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KonvertableProcessService : ProcessService {

    // TODO extract ExtensionFunction to common method
    override fun resolveKAPT(element: TypeElement): Meta {
        val classes = LinkedList<DataClass>()
        val functions = LinkedList<ExtensionFunction>()
        element.annotation<Konvertable>()?.classes?.forEach { to ->
            val toClassName = to.name
            val fromClassName = element.simpleName.toString()
            val fromClassType = element.asType().asTypeName()
            val resolvedFields = handleAnnotation(to, element.fields)

            val toClass = DataClass(
                name = toClassName,
                packageName = element.packetName,
                constructorFields = resolvedFields.map { Field(it) }
            )

            classes += toClass
            functions += ExtensionFunction(
                name = "to$toClassName",
                receiver = fromClassType,
                returns = toClass.asTypeName(),
                statement = resolvedFields.joinToString(",") {
                    String.format("%s=%s", it.simpleName, it.simpleName)
                }
            )
            functions += ExtensionFunction(
                name = "to$fromClassName",
                receiver = toClass.asTypeName(),
                returns = fromClassType,
                statement = resolvedFields.joinToString(",") {
                    String.format("%s=%s", it.simpleName, it.simpleName)
                }
            )
        }
        return KonvertableMeta(
            annotatedClass = element,
            classes = classes,
            functions = functions
        )
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