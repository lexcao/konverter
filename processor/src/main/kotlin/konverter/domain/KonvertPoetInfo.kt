package konverter.domain

import com.squareup.kotlinpoet.FunSpec
import konverter.helper.elementUtils
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

/**
 *  poet info for kotlin poet
 */
data class KonvertPoetInfo(
    val funSpec: FunSpec,
    val imports: Set<ImportInfo> = emptySet()
) {
    data class ImportInfo(
        val packageName: String,
        val name: String
    ) {

        constructor(clazz: KClass<*>) : this(
            packageName = clazz.java.`package`.name,
            name = clazz.simpleName.toString()

        )

        constructor(element: TypeElement) : this(
            packageName = elementUtils.getPackageOf(element).toString(),
            name = element.simpleName.toString()
        )
    }
}
