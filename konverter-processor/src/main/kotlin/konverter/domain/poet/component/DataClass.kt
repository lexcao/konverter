package konverter.domain.poet.component

import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.PropertySpec

data class DataClass(
    val name: String,
    val packageName: String,
    val constructorFields: Collection<Field>
) {

    val qualifiedName: String
        get() = "$packageName.$name"

    val parameters: List<ParameterSpec>
        get() = constructorFields.map {
            ParameterSpec.builder(it.name, it.type).build()
        }

    val properties: List<PropertySpec>
        get() = constructorFields.map {
            PropertySpec.builder(it.name, it.type)
                .initializer(it.name)
                .build()
        }
}