package demo.test

import konverter.annotation.Konvert

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

    // null
    val iNull: Int?,
    val sNull: Short?,
    val btNull: Byte?,
    val lNull: Long?,
    val cNull: Char?,
    val fNull: Float?,
    val dNull: Double?,
    val bNull: Boolean?,
    val stringNull: String?
)

data class FullTypeTo(
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

    val iNull: Int?,
    val sNull: Short?,
    val btNull: Byte?,
    val lNull: Long?,
    val cNull: Char?,
    val fNull: Float?,
    val dNull: Double?,
    val bNull: Boolean?,
    val stringNull: String?
)

@Konvert(FullTypeTo::class)
data class NullType(
    val nothing: Nothing? = null
)

