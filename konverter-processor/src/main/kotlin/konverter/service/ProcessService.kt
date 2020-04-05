package konverter.service

import konverter.domain.kapt.Meta
import konverter.domain.poet.Writable
import javax.lang.model.element.TypeElement

interface ProcessService {

    fun resolveKAPT(element: TypeElement): Meta
    fun resolvePoet(meta: List<Meta>): Writable
}