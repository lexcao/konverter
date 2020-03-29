package demo.test

import demo.toFiledTestA
import konverter.Konvert
import org.junit.Assert
import org.junit.Test

internal class KonvertFiledTest {

    @Test
    fun testFromFiledAnnotation() {
        val actual = FiledTestB("Tom")
        val expect = FiledTestA("Tom")

        Assert.assertEquals(expect, actual.toFiledTestA())
    }
}

data class FiledTestA(
    val username: String
)

@Konvert(to = FiledTestA::class)
data class FiledTestB(

    @Konvert.Filed("username")
    val name: String
)