package konverter.resovler

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertPoetInfo
import konverter.domain.KonvertResolvedInfo
import konverter.domain.poet.ExtensionFunction
import konverter.domain.poet.Import
import java.lang.String.format
import java.util.LinkedList
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

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

    fun buildKonvertFunction(meta: KonvertMetaInfo): KonvertPoetInfo {
        val fromClass = meta.fromClass
        val toClass = meta.toClass

        val membersMap = MetaResolver.resolveMembers(meta)

        val resolvedMembersMap = ContractResolver.apply(
            membersMap = membersMap
        )

        return buildFunction(
            toClassName = toClass.name.simpleName,
            fromType = fromClass.type,
            toType = toClass.type,
            map = resolvedMembersMap
        )
    }

    fun buildFunction(
        toClassName: String,
        fromType: TypeName,
        toType: TypeName,
        map: Map<VariableElement, KonvertResolvedInfo>
    ): KonvertPoetInfo {
        val collectedImportElements = HashSet<TypeElement>()
        val collectedImportClasses = HashSet<KClass<*>>()
        val members = map.keys.joinToString(", ") {
            val resolvedInfo = map.getValue(it)
            collectedImportClasses.addAll(resolvedInfo.importClasses)
            collectedImportElements.addAll(resolvedInfo.importElements)
            format("%s = %s", it.simpleName, resolvedInfo.expression)
        }

        val funSpec = FunSpec.builder("to" + toClassName)
            .addKdoc("Auto generated code by @Konvert annotation processor")
            .receiver(fromType)
            .addStatement("return %T($members)", toType)
            .returns(toType)
            .build()

        val imports = collectedImportClasses.map {
            KonvertPoetInfo.ImportInfo(it)
        } + collectedImportElements.map {
            KonvertPoetInfo.ImportInfo(it)
        }

        return KonvertPoetInfo(
            funSpec = funSpec,
            imports = imports.toSet()
        )
    }
}
