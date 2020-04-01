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
import konverter.handler.DateToLongHandler
import konverter.handler.KonvertByHandler
import konverter.handler.KonvertCodeHandler
import konverter.handler.SameTypeHandler
import konverter.helper.fields
import konverter.helper.isType
import konverter.helper.notNull
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

class KonvertProcessService : ProcessService<Konvert> {

    private val handlers = listOf(
        KonvertCodeHandler,
        SameTypeHandler,
        KonvertByHandler,
        AnyToStringHandler,
        DateToLongHandler
    )

    override fun resolveKAPT(element: TypeElement): Meta<Konvert> {
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
            val toType = to.asType()

            val resolvedInfo = if (from == null) {
                KonvertResolvedInfo(
                    expression = defaultValueOfType(toType)
                )
            } else {
                handlers.firstOrNull { it.support(from, to) }
                    ?.handle(from, to)
            } ?: KonvertResolvedInfo(
                expression = "\nTODO(\"[$from: ${from?.asType()}] cannot convert to [$to: $toType]\")\n"
            )

            resolved[to] = resolvedInfo
        }

        val imports = LinkedList<Import>()
        val members = to.fields.joinToString(", ") {
            val resolvedInfo = resolved.getValue(it)
            imports.addAll(resolvedInfo.importClasses.map { Import(it) } +
                    resolvedInfo.importElements.map {
                        Import(it)
                    })
            String.format("%s = %s", it.simpleName, resolvedInfo.expression)
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

    private fun defaultValueOfType(type: TypeMirror): String {
        return when (type.kind) {
            TypeKind.INT -> "0"
            TypeKind.BYTE -> "0"
            TypeKind.LONG -> "0"
            TypeKind.SHORT -> "0"
            TypeKind.FLOAT -> "0.0f"
            TypeKind.DOUBLE -> "0.0"
            TypeKind.CHAR -> "'\u0000'"
            TypeKind.BOOLEAN -> "false"
            else -> {
                // TODO handle String? = null
                if (type.isType<String>() && type.notNull()) "\"\""
                else "null"
            }
        }
    }

    override fun resolvePoet(meta: List<Meta<Konvert>>): Writable {
        val filtered = meta.filterIsInstance<KonvertMeta>()
        val flatFunctions = filtered.map { it.function }

        return KonvertWriter(meta.first().packageName, flatFunctions)
    }
}
