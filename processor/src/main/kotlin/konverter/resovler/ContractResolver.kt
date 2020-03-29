package konverter.resovler

import konverter.Konvert
import konverter.annotation.KonvertContract
import konverter.default.DefaultContract
import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertResolvedInfo
import konverter.helper.getAnnotationClassValue
import konverter.helper.isPrimitiveType
import konverter.helper.isType
import konverter.helper.notNull
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeKind

object ContractResolver {

    private fun apply(
        contract: KonvertContract?,
        toFiled: VariableElement,
        fromFiled: VariableElement?
    ): KonvertResolvedInfo {
        val toType = toFiled.asType()

        if (fromFiled == null) {
            val name = when (toType.kind) {
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

            return KonvertResolvedInfo(
                origin = fromFiled,
                expression = name
            )
        }

        // @Konvert.Code
        fromFiled.getAnnotation(Konvert.Code::class.java)?.run {
            return KonvertResolvedInfo(
                origin = fromFiled,
                expression = expression
            )
        }

        // return from as same type
        val fromType = fromFiled.asType()
        if (toType == fromType) {
            return KonvertResolvedInfo(
                origin = fromFiled,
                expression = fromFiled.simpleName.toString()
            )
        } else if (fromFiled.getAnnotation(Konvert.By::class.java) != null) {
            // @Konvert.By
            fromFiled.getAnnotationClassValue<Konvert.By> { value }.run {
                // solution 1 call convert function
                return KonvertResolvedInfo(
                    origin = fromFiled,
                    expression = "with($simpleName) { ${fromFiled.simpleName}.konvert() }",
                    importElements = listOf(this)
                )
                // TODO solution 2 replace raw string of convert function
            }
        }

        // apply anyToString contract
        if (toType.isType<String>() &&
            contract?.anyToString ?: DefaultContract.anyToString
        ) {
            return KonvertResolvedInfo(
                origin = fromFiled,
                expression = "${fromFiled.simpleName}.toString()"
            )
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
                    val name = "${fromFiled.simpleName}.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().id))"
                    return KonvertResolvedInfo(
                        origin = fromFiled,
                        expression = name,
                        importClasses = listOf(ZoneOffset::class)
                    )
                }
                fromType.isType<Date>() -> {
                    return KonvertResolvedInfo(
                        origin = fromFiled,
                        expression = "${fromFiled.simpleName}.time"
                    )
                }
                else -> return KonvertResolvedInfo(
                    origin = fromFiled,
                    expression = "${fromFiled.simpleName}.time"
                )

            }
        }

        return KonvertResolvedInfo(
            origin = fromFiled,
            expression = "\nTODO(\"[$fromFiled: $fromType] cannot convert to [$toFiled: $toType]\")\n"
        )
    }

    fun apply(
        meta: KonvertMetaInfo,
        membersMap: Map<VariableElement, VariableElement?>
    ): Map<VariableElement, KonvertResolvedInfo> {
        val result = HashMap<VariableElement, KonvertResolvedInfo>()
        membersMap.forEach { (t, u) ->
            result[t] = apply(meta.contract, t, u)
        }
        return result
    }
}