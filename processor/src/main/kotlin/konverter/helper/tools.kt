package konverter.helper

import org.jetbrains.annotations.NotNull
import javax.annotation.processing.Filer
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.element.Element
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.MirroredTypeException
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic
import kotlin.reflect.KClass

internal var init: Boolean = false
internal lateinit var elementUtils: Elements
internal lateinit var typeUtils: Types
internal lateinit var logger: Messager
internal lateinit var filer: Filer

internal fun initTools(env: ProcessingEnvironment) {
    if (init) return

    elementUtils = env.elementUtils
    typeUtils = env.typeUtils
    logger = env.messager
    filer = env.filer
    init = true
}

/**
 *  动态获取 Annotation 所修饰的 Class
 */
inline fun <reified T : Annotation> Element.getAnnotationClassValue(
    invokeClass: T.() -> KClass<*>
): TypeElement = try {
    getAnnotation(T::class.java).invokeClass()
    error("Expected to get a MirroredTypeException")
} catch (e: MirroredTypeException) {
    (e.typeMirror as DeclaredType).asElement() as TypeElement
}

inline fun <reified T : Any> TypeMirror.isPrimitiveType(): Boolean {
    return T::class.javaPrimitiveType?.let {
        it.isPrimitive && it.typeName == toString()
    } ?: false
}

inline fun <reified T : Any> TypeMirror.isType(): Boolean {
    return T::class.javaObjectType.typeName == toString()
}

fun TypeMirror.notNull(): Boolean {
    return this.getAnnotation(NotNull::class.java) == null
}

fun info(message: () -> String) {
    logger.printMessage(Diagnostic.Kind.WARNING, message())
}

inline fun <reified T : Annotation> RoundEnvironment.findAnnotatedClassElement(): List<TypeElement> {
    return getElementsAnnotatedWith(T::class.java)
        .filter { it.kind == ElementKind.CLASS }
        .filterIsInstance<TypeElement>()
}

inline fun <reified T : Annotation> VariableElement.hasAnnotation(): Boolean {
    return getAnnotation(T::class.java) != null
}

val TypeElement.packetName: String
    get() = elementUtils.getPackageOf(this).toString()

val TypeElement.fields: List<VariableElement>
    get() = enclosedElements
        .filter { it.kind == ElementKind.FIELD }
        .filterIsInstance<VariableElement>()
