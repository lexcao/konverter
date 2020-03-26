package konverter.annotation

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.SOURCE)
annotation class KonvertFiled(
    val value: String
)
