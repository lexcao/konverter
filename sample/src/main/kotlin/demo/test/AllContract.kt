package demo.test

import konverter.Konvert
import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

internal class AllContractTest {

    @Test
    fun anyToString() {
        val nowDate = Date()
        val nowLocalDateTime = LocalDateTime.now()
        val actual = ContractFrom(10, nowDate, nowLocalDateTime)
        val expect =
            ContractTo("10", nowDate.time, nowLocalDateTime.toEpochSecond(ZoneOffset.of(ZoneOffset.systemDefault().id)))

        Assert.assertEquals(expect, actual.toContractTo())
    }
}

@Konvert(ContractTo::class)
data class ContractFrom(
    val anyToStringInt: Int,
    val dateToLong: Date,
    val localDateTimeToLong: LocalDateTime
)

data class ContractTo(
    val anyToStringInt: String,
    val dateToLong: Long,
    val localDateTimeToLong: Long
)
