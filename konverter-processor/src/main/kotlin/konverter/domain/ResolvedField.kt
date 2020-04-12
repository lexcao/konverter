package konverter.domain

import konverter.Konvert
import konverter.helper.Side
import konverter.helper.annotation
import konverter.helper.defaultValueOfObject
import konverter.helper.defaultValueOfPrimitive
import konverter.helper.getAnnotationClassValue
import konverter.helper.hasAnnotation
import konverter.helper.isType
import konverter.helper.nullable
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.TypeMirror

data class ResolvedField(
    val toName: String,
    val fromName: String,
    val side: Side,
    val type: TypeMirror,
    val nullable: Boolean,
    val convertedBy: TypeElement? = null,
    val annotations: List<AnnotationMirror> = emptyList()
) {

    val hasInitializer: Boolean = false

    constructor(original: VariableElement, side: Side) : this(
        fromName = original.simpleName.toString(),
        toName = original.annotation<Konvert.Field>()?.name
            ?: original.simpleName.toString(),
        type = original.asType(),
        side = side,
        nullable = original.nullable(),
        convertedBy = if (original.hasAnnotation<Konvert.By>()) original.getAnnotationClassValue<Konvert.By> { value } else null,
        annotations = original.annotationMirrors
    )

    val defaultValue: String
        get() = with(type) {
            when {
                kind.isPrimitive -> defaultValueOfPrimitive
                nullable -> "null"
                isType<String>() -> "\"\""
                else -> defaultValueOfObject
            }
        }
}
