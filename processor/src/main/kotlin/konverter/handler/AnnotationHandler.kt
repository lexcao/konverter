package konverter.handler

import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

interface AnnotationHandler {

    fun support(from: VariableElement, to: VariableElement): Boolean

    fun handle(from: VariableElement, to: VariableElement): KonvertResolvedInfo
}