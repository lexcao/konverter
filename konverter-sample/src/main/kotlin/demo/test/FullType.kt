package demo.test

import konverter.Konvert
import org.junit.Assert
import org.junit.Test

internal class FullTypeTest {

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
            obj = ObjectType(""),
            string = "",
            list = emptyList(),

            nil = null,
            iNull = null,
            sNull = null,
            btNull = null,
            lNull = null,
            cNull = null,
            fNull = null,
            dNull = null,
            bNull = null,
            stringNull = null,
            objNull = null,
            listNull = null
        )
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
            obj = ObjectType("object"),
            list = listOf("list"),
            iNull = 1,
            sNull = 2,
            btNull = 3,
            lNull = 4,
            cNull = 'a',
            fNull = 5.0f,
            dNull = 6.0,
            bNull = true,
            stringNull = "7",
            objNull = ObjectType("other-object"),
            listNull = listOf("a", "b")
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
            obj = ObjectType("object"),
            list = listOf("list"),

            iNull = 1,
            sNull = 2,
            btNull = 3,
            lNull = 4,
            cNull = 'a',
            fNull = 5.0f,
            dNull = 6.0,
            bNull = true,
            stringNull = "7",
            objNull = ObjectType("other-object"),
            listNull = listOf("a", "b")
        )

        Assert.assertEquals(expect, actual.toFullTypeTo())
    }
}

@Konvert(FullTypeTo::class)
data class FullTypeFrom(
    val i: Int,
    val s: Short,
    val bt: Byte,
    val l: Long,
    val c: Char,
    val f: Float,
    val d: Double,
    val b: Boolean,
    val string: String,
    val nil: Any?,
    val obj: ObjectType,
    val list: List<String>,

    // null
    val iNull: Int?,
    val sNull: Short?,
    val btNull: Byte?,
    val lNull: Long?,
    val cNull: Char?,
    val fNull: Float?,
    val dNull: Double?,
    val bNull: Boolean?,
    val stringNull: String?,
    val objNull: ObjectType?,
    val listNull: List<String>?
)

data class FullTypeTo(
    val i: Int,
    val s: Short,
    val bt: Byte,
    val l: Long,
    val f: Float,
    val c: Char,
    val d: Double,
    val b: Boolean,
    val string: String,
    val nil: Any?,
    val obj: ObjectType,
    val list: List<String>,

    val iNull: Int?,
    val sNull: Short?,
    val btNull: Byte?,
    val lNull: Long?,
    val cNull: Char?,
    val fNull: Float?,
    val dNull: Double?,
    val bNull: Boolean?,
    val stringNull: String?,
    val objNull: ObjectType?,
    val listNull: List<String>?
)

@Konvert(FullTypeTo::class)
data class NullType(
    val nothing: Unit? = null
)

data class ObjectType(
    val name: String
)
