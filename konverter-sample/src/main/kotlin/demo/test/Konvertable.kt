package demo.test

import konverter.Konvertable
import konverter.To
import org.junit.Assert
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

internal class KonvertableTest {

    @Test
    fun konvertable() {
        val given = KonvertableA("1", "A", 20, LocalDate.now())
        Assert.assertEquals(
            given.toKonvertableB().toKonvertableA(),
            given
        )

    }
}

@Konvertable(
    To(name = "KonvertableB", omit = ["birthday"])
)
data class KonvertableA(
    val id: String,
    val name: String,
    val age: Int,
    val birthday: LocalDate?
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
