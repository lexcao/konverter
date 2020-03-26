package konverter.contract

sealed class ContractCause(message: String) : Throwable(message) {

    data class MissingFiled(val field: String) : ContractCause(
        "MissingFiled: convert to field [$field] which in from is not present"
    )
}