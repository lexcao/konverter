package demo

import konverter.Konvert
import konverter.Konvertable
import konverter.To

@Konvert(to = B::class)
data class A(
    val id: String,
    val name: String
)

@Konvert(to = C::class)
data class B(
    val id: String,
    val name: String
)

@Konvert(to = A::class)
@Konvertable(To("D"))
data class C(
    val id: String,
    val name: String,
    val age: Int
)