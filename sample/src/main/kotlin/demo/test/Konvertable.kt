package demo.test

import konverter.Konvertable
import org.junit.Test
import java.time.LocalDateTime

internal class KonvertableTest {

    @Test
    fun konvertable() {

    }
}

@Konvertable("KonvertableB", omit = ["birthday"])
data class KonvertableA(
    val id: String,
    val name: String,
    val age: Int,
    val birthday: LocalDateTime
)

@Konvertable("KonvertableD", pick = ["name"])
data class KonvertableC(
    val id: String,
    val name: String,
    val age: Int,
    val birthday: LocalDateTime
)
