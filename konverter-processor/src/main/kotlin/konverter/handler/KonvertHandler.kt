package konverter.handler

import com.squareup.kotlinpoet.asTypeName
import konverter.domain.CompositeResolvedInfo
import konverter.domain.KonvertResolvedInfo
import konverter.domain.Resolved
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Import
import konverter.helper.defaultValue
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

interface KonvertHandler {

    fun support(from: VariableElement, to: VariableElement): Boolean

    fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo

    companion object {

        private val handlers = listOf(
            KonvertCodeHandler,
            SameTypeHandler,
            KonvertByHandler,
            AnyToStringHandler,
            DefaultHandler
        )

        fun handle(fieldsMap: Map<Resolved, Resolved?>): Map<Resolved, KonvertResolvedInfo> {
            return fieldsMap.map { (to, from) ->
                val resolvedTo = if (from == null) {
                    KonvertResolvedInfo(
                        expression = to.original.defaultValue
                    )
                } else {
                    handlers.first { it.support(from.original, to.original) }
                        .handle(from.original, to.original)
                }
                to to resolvedTo
            }.toMap()
        }

        fun handle(resolve: CompositeResolvedInfo): List<ExtensionFunction> {
            return listOf(
                buildFunction(
                    from = resolve.fromElement,
                    to = resolve.toElement,
                    fieldsMap = resolve.to2From
                ), buildFunction(
                    from = resolve.toElement,
                    to = resolve.fromElement,
                    fieldsMap = resolve.from2To
                )
            )
        }

        private fun buildFunction(
            from: TypeElement,
            to: TypeElement,
            fieldsMap: Map<Resolved, Resolved?>
        ): ExtensionFunction {

            val imports = LinkedList<Import>()
            val resolved = handle(fieldsMap)

            val members = fieldsMap.keys.joinToString(",") {
                val resolvedInfo = resolved.getValue(it)
                String.format("%s=%s", it.name, resolvedInfo.expression)
            }

            return ExtensionFunction(
                name = "to${to.simpleName}",
                returns = to.asType().asTypeName(),
                receiver = from.asType().asTypeName(),
                statement = members,
                imports = imports
            )
        }
    }
}