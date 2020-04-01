package konverter.handler

import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

interface AnnotationHandler {

    val from: VariableElement
    val to: VariableElement

    fun support(): Boolean

    fun handle(): KonvertResolvedInfo
}