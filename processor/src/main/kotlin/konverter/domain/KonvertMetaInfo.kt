package konverter.domain

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.asClassName
import com.squareup.kotlinpoet.asTypeName
import konverter.Konvert
import konverter.helper.getAnnotationClassValue
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

/**
 *  meta info processed by kapt
 */
data class KonvertMetaInfo(
    val annotatedClass: TypeElement
) {
    val fromClass = ClassInfo(annotatedClass)
    val toClass = ClassInfo(annotatedClass.getAnnotationClassValue<Konvert> { to })

    data class ClassInfo(
        val origin: TypeElement
    ) {
        val name: ClassName = origin.asClassName()
        val type: TypeName = origin.asType().asTypeName()

        val members: List<VariableElement> = origin.enclosedElements
            .filter { it.kind == ElementKind.FIELD }
            .filterIsInstance<VariableElement>()
    }
}

