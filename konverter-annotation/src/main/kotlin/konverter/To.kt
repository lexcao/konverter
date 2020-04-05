package konverter

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class To(

    /**
     *  the name of the generated data class
     */
    val name: String,

    /**
     *  the filed to be omitted
     *  empty means nothing omitted
     */
    val omit: Array<String> = [],

    /**
     *  the filed to pick with
     *  empty means full fields picked
     */
    val pick: Array<String> = []
)
