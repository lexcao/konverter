package konverter.service

import konverter.domain.BuildFunctionInfo
import konverter.domain.CompositeResolvedInfo
import konverter.domain.kapt.KonvertMeta
import konverter.domain.kapt.Meta
import konverter.domain.poet.KonvertWriter
import konverter.domain.poet.Writable
import konverter.handler.KonvertHandler
import konverter.helper.Side
import javax.lang.model.element.TypeElement

class KonvertProcessService : ProcessService {

    override fun resolveKAPT(element: TypeElement): Meta {
        val meta = KonvertMeta(element)
        val from = meta.annotatedClass
        val to = meta.toClass

        val functions = KonvertHandler.handle(
            CompositeResolvedInfo(
                from = BuildFunctionInfo(from, Side.FROM),
                to = BuildFunctionInfo(to, Side.TO)
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
