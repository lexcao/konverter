package demo.test

import konverter.Konvertable
import konverter.To
import org.junit.Test
import java.time.LocalDateTime

internal class KonvertableTest {

    @Test
    fun konvertable() {

    }
}

@Konvertable(
    To(name = "KonvertableB", omit = ["birthday"])
)
data class KonvertableA(
    val id: String,
    val name: String,
    val age: Int,
    val birthday: LocalDateTime
)

@Konvertable(
    To(name = "KonvertableD", pick = ["name"]),
    To(name = "KonvertableS", pick = ["id", "age"])
)
data class KonvertableC(
    val id: String,
    val name: String,
    val age: Int,
    val birthday: LocalDateTime
)
