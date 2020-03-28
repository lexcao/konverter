package konverter.domain

import com.squareup.kotlinpoet.FunSpec
import kotlin.reflect.KClass

/**
 *  poet info for kotlin poet
 */
data class KonvertPoetInfo(
    val funSpec: FunSpec,
    val imports: Set<ImportInfo> = emptySet()
) {
    data class ImportInfo(
        val clazz: KClass<*>
    ) {
        val packageName: String = clazz.java.packageName
        val name: String = clazz.simpleName.toString()
    }
}
