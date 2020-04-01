package konverter.domain.kapt

import konverter.helper.packetName
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

interface Meta<T : Annotation> {

    val annotatedClass: TypeElement
    val clazz: KClass<T>
    val target: T
        get() = annotatedClass.getAnnotation(clazz.java)
    val packageName: String
        get() = annotatedClass.packetName
}