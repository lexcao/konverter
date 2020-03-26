package demo.test

import demo.toFullTypeTo
import org.junit.Assert
import org.junit.Test

internal class KonvertTest {

    @Test
    fun `full type with default value`() {
        val nothing = NullType()

        val expect = FullTypeTo(
            i = 0,
            s = 0,
            bt = 0,
            l = 0,
            c = '\u0000',
            f = 0.0f,
            d = 0.0,
            b = false,
            string = "",
            nil = null,
            iNull = null,
            sNull = null,
            btNull = null,
            lNull = null,
            cNull = null,
            fNull = null,
            dNull = null,
            bNull = null,
            stringNull = null
        )

        Assert.assertEquals(expect, nothing.toFullTypeTo())
    }

    @Test
    fun `full type`() {
        val actual = FullTypeFrom(
            i = 1,
            s = 2,
            bt = 3,
            l = 4,
            c = 'a',
            f = 5.0f,
            d = 6.0,
            b = true,
            string = "7",
            nil = Unit,
            iNull = 1,
            sNull = 2,
            btNull = 3,
            lNull = 4,
            cNull = 'a',
            fNull = 5.0f,
            dNull = 6.0,
            bNull = true,
            stringNull = "7"
        )

        val expect = FullTypeTo(
            i = 1,
            s = 2,
            bt = 3,
            l = 4,
            c = 'a',
            f = 5.0f,
            d = 6.0,
            b = true,
            string = "7",
            nil = Unit,
            iNull = 1,
            sNull = 2,
            btNull = 3,
            lNull = 4,
            cNull = 'a',
            fNull = 5.0f,
            dNull = 6.0,
            bNull = true,
            stringNull = "7"
        )

        Assert.assertEquals(expect, actual.toFullTypeTo())
    }
}