package konverter.handler

import konverter.Konvert
import konverter.domain.KonvertResolvedInfo
import javax.lang.model.element.VariableElement

class KonvertCodeHandler(
    override val from: VariableElement,
    override val to: VariableElement
) : AnnotationHandler {

    private lateinit var target: Konvert.Code

    override fun support(): Boolean {
        val target = from.getAnnotation(Konvert.Code::class.java)?.also {
            target = it
        }
        return target != null
    }

    override fun handle(): KonvertResolvedInfo {
        return KonvertResolvedInfo(
            target.expression
        )
    }
}