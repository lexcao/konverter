package konverter.helper

import com.squareup.kotlinpoet.FileSpec
import konverter.builder.KotlinBuilder

fun kotlin(
    packageName: String,
    fileName: String,
    builder: KotlinBuilder.() -> Unit
): FileSpec {
    return KotlinBuilder(packageName, fileName).apply(builder).build()
}
