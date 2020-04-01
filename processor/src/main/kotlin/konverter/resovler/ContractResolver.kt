package konverter.resovler

import konverter.domain.KonvertResolvedInfo
import konverter.handler.AnyToStringHandler
import konverter.handler.DateToLongHandler
import konverter.handler.KonvertByHandler
import konverter.handler.KonvertCodeHandler
import konverter.handler.SameTypeHandler
import konverter.helper.isType
import konverter.helper.notNull
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror

object ContractResolver {

    private val handlers = listOf(
        KonvertCodeHandler,
        SameTypeHandler,
        KonvertByHandler,
        AnyToStringHandler,
        DateToLongHandler
    )

    private fun apply(
        toFiled: VariableElement,
        fromFiled: VariableElement?
    ): KonvertResolvedInfo {
        val toType = toFiled.asType()

        if (fromFiled == null) {
            return KonvertResolvedInfo(
                expression = defaultValueOfType(toFiled.asType())
            )
        }

        handlers.forEach {
            if (it.support(fromFiled, toFiled)) {
                return it.handle(fromFiled, toFiled)
            }
        }

        return KonvertResolvedInfo(
            expression = "\nTODO(\"[$fromFiled: ${fromFiled.asType()}] cannot convert to [$toFiled: $toType]\")\n"
        )
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

    fun apply(
        membersMap: Map<VariableElement, VariableElement?>
    ): Map<VariableElement, KonvertResolvedInfo> {
        val result = HashMap<VariableElement, KonvertResolvedInfo>()
        membersMap.forEach { (t, u) ->
            result[t] = apply(t, u)
        }
        return result
    }
}