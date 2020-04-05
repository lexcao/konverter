package konverter.domain.poet

import javax.annotation.processing.Filer

interface Writable {
    val poets: Collection<KotlinPoet>
    fun write(filer: Filer) {
        poets.forEach {
            it.write().writeTo(filer)
        }
    }
}