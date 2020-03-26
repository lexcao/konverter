package konverter.annotation

import konverter.default.DefaultContract

/**
 *  convert one type to another when apply the contract
 *  default contract is applied when missing
 *  @see konverter.default.DefaultContract
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class KonvertContract(

/*
    */
    /**
     *  use the default value to the `to` class if filed found missing
     *  otherwise throw errors
     *//*

    val defaultIfMissingField: Boolean = DefaultContract.defaultIfMissingField,
*/

    /**
     *  date to epoch seconds
     */
    val dateToEpochSeconds: Boolean = DefaultContract.dateToEpochSeconds,

    /**
     *  use toString() on any type when to String type if the type is not matched
     */
    val anyToString: Boolean = DefaultContract.anyToString
)
