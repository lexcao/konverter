package demo.test

import konverter.Konvert
import org.junit.Assert
import org.junit.Test

internal class AllContractTest {

    @Test
    fun anyToString() {
        val actual = AnyToStringA(10)
        val expect = AnyToStringB("10")

        Assert.assertEquals(expect, actual.toAnyToStringB())
    }
}

@Konvert(AnyToStringB::class)
data class AnyToStringA(
    val anyToStringInt: Int
)

data class AnyToStringB(
    val anyToStringInt: String
)
