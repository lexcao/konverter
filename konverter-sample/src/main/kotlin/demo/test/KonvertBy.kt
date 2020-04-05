package demo.test

import konverter.Konvert
import org.junit.Assert
import org.junit.Test

internal class KonvertByTest {

    @Test
    fun convertBy() {
        val actual = ByTestB(ByTestEnum.B)
        val expect = ByTestA(1)

        Assert.assertEquals(expect, actual.toByTestA())
    }
}

data class ByTestA(
    val status: Int
)

@Konvert(to = ByTestA::class)
data class ByTestB(

    @Konvert.By(EnumToOrdinal::class)
    val status: ByTestEnum
)

enum class ByTestEnum {
    A, B
}

object EnumToOrdinal : Konvert.KonvertBy<ByTestEnum, Int> {
    override fun ByTestEnum.konvert(): Int {
        return ordinal
    }
}

