package konverter.resovler

import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertResolvedInfo
import konverter.domain.poet.ExtensionFunction
import konverter.domain.poet.Import
import java.lang.String.format
import java.util.LinkedList
import javax.lang.model.element.VariableElement

object PoetResolver {

    fun resolve(
        meta: KonvertMetaInfo,
        resolvedMembersMap: Map<VariableElement, KonvertResolvedInfo>
    ): ExtensionFunction {
        val toClass = meta.toClass
        val imports = LinkedList<Import>()
        val members = toClass.members.joinToString(", ") {
            val resolvedInfo = resolvedMembersMap.getValue(it)
            imports.addAll(resolvedInfo.importClasses.map { Import(it) } + resolvedInfo.importElements.map { Import(it) })
            format("%s = %s", it.simpleName, resolvedInfo.expression)
        }

        return ExtensionFunction(
            name = "to${toClass.name.simpleName}",
            returns = toClass.type,
            receiver = meta.fromClass.type,
            statement = members,
            imports = imports
        )
    }
}
