package konverter.resovler

import konverter.annotation.KonvertContract
import konverter.default.DefaultContract
import konverter.helper.isType
import konverter.helper.notNull
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

object ContractResolver {

    fun apply(
        contract: KonvertContract?,
        toFiled: VariableElement,
        fromFiled: VariableElement?
    ): String {
        val toType = toFiled.asType()

        if (fromFiled == null) {
            return when (toType.kind) {
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
                    if (toType.isType<String>() && toType.notNull()) "\"\""
                    else "null"
                }
            }
        }

        // return from as same type
        val fromType = fromFiled.asType()
        if (toType == fromType) {
            return fromFiled.simpleName.toString()
        }

        // apply anyToString contract
        if (toType.kind == TypeKind.DECLARED &&
            toType.isType<String>() &&
            contract?.anyToString ?: DefaultContract.anyToString
        ) {
            return "${fromFiled.simpleName}.toString()"
        }

        // dateToEpochSeconds
        if (toType.toString() == "java.lang.lang" &&
            fromType.kind == TypeKind.DECLARED &&
            (fromType.toString() == "java.time.LocalDateTime" ||
                    fromType.toString() == "java.util.Date") &&
            contract?.dateToEpochSeconds ?: DefaultContract.dateToEpochSeconds
        ) {
            return "${fromFiled.simpleName}.toString()"
        }

        return "null"
    }

    fun apply(
        contract: KonvertContract?,
        membersMap: Map<VariableElement, VariableElement?>
    ): Map<VariableElement, String> {
        val result = HashMap<VariableElement, String>()
        membersMap.forEach { (t, u) ->
            result[t] = apply(contract, t, u)
        }
        return result
    }
}