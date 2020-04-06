package konverter.domain.kapt

import konverter.helper.packetName
import javax.lang.model.element.TypeElement

interface Meta {

    val annotatedClass: TypeElement
    val packageName: String
        get() = annotatedClass.packetName
}
