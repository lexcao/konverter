package konverter.handler

import com.squareup.kotlinpoet.asTypeName
import konverter.domain.BuildFunctionInfo
import konverter.domain.CompositeResolvedInfo
import konverter.domain.KonvertResolvedInfo
import konverter.domain.ResolvedField
import konverter.domain.poet.component.ExtensionFunction
import konverter.domain.poet.component.Import
import konverter.domain.poet.component.Param
import konverter.helper.javaToKotlinType
import java.util.LinkedList

interface KonvertHandler {

    fun support(from: ResolvedField, to: ResolvedField): Boolean

    fun handle(context: HandlerContext): KonvertResolvedInfo

    companion object {

        private val handlers = listOf(
            SameTypeHandler,
            KonvertByHandler,
            AnyToStringHandler,
            DefaultHandler
        )

        private fun handle(
            functionName: String,
            fieldsMap: Map<ResolvedField, ResolvedField?>
        ): Map<ResolvedField, KonvertResolvedInfo> {
            return fieldsMap.map { (to, from) ->
                val resolvedTo = if (from == null) {
                    KonvertResolvedInfo(
                        expression = to.defaultValue
                    )
                } else {
                    val context = HandlerContext(
                        from = from,
                        to = to,
                        implicitThis = "this@$functionName"
                    )
                    handlers.first { it.support(from, to) }
                        .handle(context)
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
            val params = LinkedList<Param>()
            val functionName = "to${to.name}"

            val resolved = handle(functionName, fieldsMap)

            val statement = fieldsMap.keys.joinToString(",") {
                val resolvedInfo = resolved.getValue(it)
                imports.addAll(resolvedInfo.importElements.map { Import(it) })
                imports.addAll(resolvedInfo.importClasses.map { Import(it) })
                params.add(
                    Param(
                        name = it.fromName,
                        expression = resolvedInfo.expression,
                        type = it.type.asTypeName()
                            .javaToKotlinType()
                            .copy(nullable = it.nullable)
                    )
                )
                String.format("%s=%s", it.fromName, it.fromName)
            }

            return ExtensionFunction(
                name = functionName,
                returns = to.type,
                receiver = from.type,
                statement = statement,
                imports = imports,
                params = params
            )
        }
    }
}