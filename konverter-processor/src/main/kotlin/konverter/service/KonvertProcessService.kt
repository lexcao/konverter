package konverter.service

import konverter.Konvert
import konverter.domain.CompositeResolvedInfo
import konverter.domain.Resolved
import konverter.domain.kapt.KonvertMeta
import konverter.domain.kapt.Meta
import konverter.domain.poet.KonvertWriter
import konverter.domain.poet.Writable
import konverter.handler.KonvertHandler
import konverter.helper.annotation
import konverter.helper.fields
import javax.lang.model.element.TypeElement

class KonvertProcessService : ProcessService {

    override fun resolveKAPT(element: TypeElement): Meta {
        val meta = KonvertMeta(element)
        val from = meta.annotatedClass
        val to = meta.toClass

        val resolvedFromFields = from.fields.map {
            val name = it.annotation<Konvert.Field>()?.name
                ?: it.simpleName.toString()
            Resolved(
                original = it,
                name = name
            )
        }

        val resolvedToFields = to.fields.map {
            Resolved(original = it)
        }

        val functions = KonvertHandler.handle(
            CompositeResolvedInfo(
                fromElement = from,
                toElement = to,
                fromField = resolvedFromFields,
                toField = resolvedToFields
            )
        )

        return meta.apply {
            this.functions = functions
        }

    }

    override fun resolvePoet(meta: List<Meta>): Writable {
        val filtered = meta.filterIsInstance<KonvertMeta>()
        val flatFunctions = filtered.flatMap { it.functions }

        return KonvertWriter(meta.first().packageName, flatFunctions)
    }
}
