package demo.test

import konverter.Konvert
import org.junit.Assert
import org.junit.Test


internal class KonvertCodeTest {

    @Test
    fun konvertCode() {
        val actual = CodeTestB(CodeTestEnum.B)
        val expect = CodeTestA(1)

        Assert.assertEquals(expect, actual.toCodeTestA())
    }
}

data class CodeTestA(
    val status: Int
)

enum class CodeTestEnum {
    A, B
}

@Konvert(to = CodeTestA::class)
data class CodeTestB(

    @Konvert.Code("status.ordinal")
    val status: CodeTestEnum
)