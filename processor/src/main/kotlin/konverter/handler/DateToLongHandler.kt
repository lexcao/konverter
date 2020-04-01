package konverter.handler

import konverter.domain.KonvertResolvedInfo
import konverter.helper.isPrimitiveType
import konverter.helper.isType
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import javax.lang.model.element.VariableElement

class DateToLongHandler(
    override val from: VariableElement,
    override val to: VariableElement
) : AnnotationHandler {

    private val fromType = from.asType()
    private val toType = to.asType()

    override fun support(): Boolean {
        val toIsLong = toType.isPrimitiveType<Long>() || toType.isType<Long>()
        val fromIsDate = fromType.isType<LocalDateTime>() || fromType.isType<Date>()
        return toIsLong && fromIsDate
    }

    override fun handle(): KonvertResolvedInfo {
        return if (fromType.isType<LocalDateTime>()) {
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