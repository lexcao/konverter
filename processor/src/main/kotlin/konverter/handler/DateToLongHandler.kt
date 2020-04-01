package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.helper.isPrimitiveType
import konverter.helper.isType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.lang.model.element.VariableElement

object DateToLongHandler : AnnotationHandler {

    override fun support(from: VariableElement, to: VariableElement): Boolean {
        val toType = to.asType()
        val fromType = from.asType()

        val toIsLong = toType.isPrimitiveType<Long>() || toType.isType<Long>()
        val fromIsDate = fromType.isType<LocalDateTime>() || fromType.isType<Date>()
        return toIsLong && fromIsDate
    }

    override fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo {
        return if (from.asType().isType<LocalDateTime>()) {
            val name = "${from.simpleName}.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().id))"
            KonvertResolvedInfo(
                expression = name,
                importClasses = listOf(ZoneOffset::class)
            )
        } else KonvertResolvedInfo(
            expression = "${from.simpleName}.time"
        )
    }
}