package demo.test

import konverter.annotation.Konvert

@Konvert(ContractTo::class)
data class ContractFrom(
    val anyToStringInt: Int
)

data class ContractTo(
    val anyToStringInt: String
)