package konverter.service

import com.squareup.kotlinpoet.asTypeName
import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import konverter.domain.kapt.KonvertMeta
import konverter.domain.kapt.Meta
import konverter.domain.poet.KonvertWriter
import konverter.domain.poet.Writable
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Import
import konverter.handler.AnyToStringHandler
import konverter.handler.DefaultHandler
import konverter.handler.KonvertByHandler
import konverter.handler.KonvertCodeHandler
import konverter.handler.SameTypeHandler
import konverter.helper.defaultValue
import konverter.helper.fields
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

class KonvertProcessService : ProcessService {

    private val handlers = listOf(
        KonvertCodeHandler,
        SameTypeHandler,
        KonvertByHandler,
        AnyToStringHandler,
        DefaultHandler
    )

    override fun resolveKAPT(element: TypeElement): Meta {
        val meta = KonvertMeta(element)
        val from = meta.annotatedClass
        val to = meta.toClass

        val fromMembersMap = from.fields.associateBy {
            it.getAnnotation(Konvert.Field::class.java)?.name
                ?: it.simpleName.toString()
        }

        val to2FromFields = to.fields.associateBy(
            keySelector = { it },
            valueTransform = { fromMembersMap[it.simpleName.toString()] }
        )

        val resolved = HashMap<VariableElement, KonvertResolvedInfo>()
        to2FromFields.forEach { (to, from) ->

            val resolvedInfo = if (from == null) {
                KonvertResolvedInfo(
                    expression = to.defaultValue
                )
            } else {
                handlers.first { it.support(from, to) }
                    .handle(from, to)
            }

            resolved[to] = resolvedInfo
        }

        val imports = LinkedList<Import>()
        val members = to.fields.joinToString(",") {
            val resolvedInfo = resolved.getValue(it)
            imports.addAll(resolvedInfo.importClasses.map { Import(it) } +
                    resolvedInfo.importElements.map {
                        Import(it)
                    })
            String.format("%s=%s", it.simpleName, resolvedInfo.expression)
        }

        return meta.apply {
            function = ExtensionFunction(
                name = "to${toClass.simpleName}",
                returns = to.asType().asTypeName(),
                receiver = from.asType().asTypeName(),
                statement = members,
                imports = imports
            )
        }
    }

    override fun resolvePoet(meta: List<Meta>): Writable {
        val filtered = meta.filterIsInstance<KonvertMeta>()
        val flatFunctions = filtered.map { it.function }

        return KonvertWriter(meta.first().packageName, flatFunctions)
    }
}
