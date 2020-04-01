package konverter.service

import konverter.domain.kapt.Meta
import konverter.domain.poet.Writable
import javax.lang.model.element.TypeElement

interface ProcessService<T : Annotation> {

    fun resolveKAPT(element: TypeElement): Meta<T>
    fun resolvePoet(meta: List<Meta<T>>): Writable
}