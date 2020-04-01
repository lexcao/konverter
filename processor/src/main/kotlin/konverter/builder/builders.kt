package konverter.builder

import com.squareup.kotlinpoet.FileSpec

fun kotlin(
    packageName: String,
    fileName: String,
    builder: KotlinBuilder.() -> Unit
): FileSpec = KotlinBuilder(packageName, fileName).apply(builder).build()
