package konverter.resovler

import konverter.annotation.KonvertContract
import konverter.default.DefaultContract
import konverter.helper.isPrimitiveType
import konverter.helper.isType
import konverter.helper.notNull
import java.time.LocalDateTime
import java.util.Date
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

object ContractResolver {

    private fun apply(
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
        if (toType.isType<String>() &&
            contract?.anyToString ?: DefaultContract.anyToString
        ) {
            return "${fromFiled.simpleName}.toString()"
        }

        // dateToEpochSeconds
        if ((toType.isPrimitiveType<Long>() ||
                    toType.isType<Long>()) &&
            (fromType.isType<LocalDateTime>() ||
                    fromType.isType<Date>()) &&
            contract?.dateToEpochSeconds ?: DefaultContract.dateToEpochSeconds
        ) {
            when {
                fromType.isType<LocalDateTime>() -> {
                    return "${fromFiled.simpleName}.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().id))"
                }
                fromType.isType<Date>() -> {
                    return "${fromFiled.simpleName}.time"
                }
                else -> return "not found in when"
            }
        }

//        return "\nTODO(\"[$fromFiled: $fromType] cannot convert to [$toFiled: $toType]\")\n"

        return toType.isType<Long>().toString()
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