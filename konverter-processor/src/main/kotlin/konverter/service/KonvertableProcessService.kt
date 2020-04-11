package konverter.service

import com.squareup.kotlinpoet.asTypeName
import konverter.Konvertable
import konverter.To
import konverter.domain.BuildFunctionInfo
import konverter.domain.CompositeResolvedInfo
import konverter.domain.ResolvedField
import konverter.domain.kapt.KonvertableMeta
import konverter.domain.kapt.Meta
import konverter.domain.poet.KonvertableWriter
import konverter.domain.poet.Writable
import konverter.domain.poet.component.DataClass
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Field
import konverter.handler.KonvertHandler
import konverter.helper.Side
import konverter.helper.annotation
import konverter.helper.asTypeName
import konverter.helper.fields
import konverter.helper.nullable
import konverter.helper.packetName
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KonvertableProcessService : ProcessService {

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
            val toClassType = toClass.asTypeName()

            classes += toClass

            val fromFields = element.fields.map {
                ResolvedField(
                    toName = it.simpleName.toString(),
                    fromName = it.simpleName.toString(),
                    type = it.asType(),
                    side = Side.FROM,
                    nullable = it.nullable()
                )
            }

            val toFields = resolvedFields.map {
                ResolvedField(
                    toName = it.simpleName.toString(),
                    fromName = it.simpleName.toString(),
                    type = it.asType(),
                    side = Side.TO,
                    nullable = it.nullable()
                )
            }

            functions += KonvertHandler.handle(
                CompositeResolvedInfo(
                    from = BuildFunctionInfo(
                        name = fromClassName,
                        type = fromClassType,
                        fields = fromFields
                    ),
                    to = BuildFunctionInfo(
                        name = toClassName,
                        type = toClassType,
                        fields = toFields
                    )
                )
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