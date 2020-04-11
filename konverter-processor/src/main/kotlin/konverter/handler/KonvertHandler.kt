package konverter.handler

import konverter.domain.BuildFunctionInfo
import konverter.domain.CompositeResolvedInfo
import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Import
import java.util.LinkedList

interface KonvertHandler {

    fun support(from: ResolvedField, to: ResolvedField): Boolean

    fun handle(from: ResolvedField, to: ResolvedField): KonvertResolvedInfo

    companion object {

        private val handlers = listOf(
            SameTypeHandler,
            KonvertByHandler,
            AnyToStringHandler,
            DefaultHandler
        )

        fun handle(fieldsMap: Map<ResolvedField, ResolvedField?>): Map<ResolvedField, KonvertResolvedInfo> {
            return fieldsMap.map { (to, from) ->
                val resolvedTo = if (from == null) {
                    KonvertResolvedInfo(
                        expression = to.defaultValue
                    )
                } else {
                    handlers.first { it.support(from, to) }
                        .handle(from, to)
                }
                to to resolvedTo
            }.toMap()
        }

        fun handle(resolve: CompositeResolvedInfo): List<ExtensionFunction> {
            return listOf(
                buildFunction(
                    from = resolve.from,
                    to = resolve.to,
                    fieldsMap = resolve.to2From
                ), buildFunction(
                    from = resolve.to,
                    to = resolve.from,
                    fieldsMap = resolve.from2To
                )
            )
        }

        private fun buildFunction(
            from: BuildFunctionInfo,
            to: BuildFunctionInfo,
            fieldsMap: Map<ResolvedField, ResolvedField?>
        ): ExtensionFunction {

            val imports = LinkedList<Import>()
            val resolved = handle(fieldsMap)

            val statement = fieldsMap.keys.joinToString(",") {
                val resolvedInfo = resolved.getValue(it)
                String.format("%s=%s", it.fromName, resolvedInfo.expression)
            }

            return ExtensionFunction(
                name = "to${to.name}",
                returns = to.type,
                receiver = from.type,
                statement = statement,
                imports = imports
            )
        }
    }
}