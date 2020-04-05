package konverter.domain.poet

import com.squareup.kotlinpoet.FileSpec

interface KotlinPoet {
    val fileName: String
    val packageName: String

    fun write(): FileSpec
}
