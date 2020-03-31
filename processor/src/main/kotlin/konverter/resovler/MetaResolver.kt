package konverter.resovler

import konverter.Konvert
import konverter.domain.KonvertMetaInfo
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement

object MetaResolver {

    fun resolveKonvert(it: TypeElement): KonvertMetaInfo {
        return KonvertMetaInfo(it)
    }

    fun resolveMembers(meta: KonvertMetaInfo): Map</*toClass*/VariableElement, /*fromClass*/VariableElement?> {
        val from = meta.fromClass
        val to = meta.toClass

        val fromMembersMap = from.members.associateBy {
            it.getAnnotation(Konvert.Field::class.java)?.name
                ?: it.simpleName.toString()
        }

        return to.members.associateBy(
            keySelector = { it },
            valueTransform = { fromMembersMap[it.simpleName.toString()] }
        )
    }
}