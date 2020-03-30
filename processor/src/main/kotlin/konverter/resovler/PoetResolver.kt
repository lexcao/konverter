package konverter.resovler

import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertPoetInfo
import konverter.domain.KonvertResolvedInfo
import java.lang.String.format
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import kotlin.reflect.KClass

object PoetResolver {

    fun resolve(
        meta: KonvertMetaInfo,
        resolvedMembersMap: Map<VariableElement, KonvertResolvedInfo>
    ): KonvertPoetInfo {
        val fromClass = meta.fromClass
        val toClass = meta.toClass

        val collectedImportElements = HashSet<TypeElement>()
        val collectedImportClasses = HashSet<KClass<*>>()
        val members = toClass.members.joinToString(", ") {
            val resolvedInfo = resolvedMembersMap.getValue(it)
            collectedImportClasses.addAll(resolvedInfo.importClasses)
            collectedImportElements.addAll(resolvedInfo.importElements)
            format("%s = %s", it.simpleName, resolvedInfo.expression)
        }

        val funSpec = FunSpec.builder("to" + toClass.name.simpleName)
            .addKdoc("Auto generated code by @Konvert annotation processor")
            .receiver(fromClass.type)
            .addStatement("return %T($members)", toClass.type)
            .returns(toClass.type)
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

    fun buildKonvertFunction(meta: KonvertMetaInfo): KonvertPoetInfo {
        val fromClass = meta.fromClass
        val toClass = meta.toClass

        val membersMap = MetaResolver.resolveMembers(meta)

        val resolvedMembersMap = ContractResolver.apply(
            meta = meta,
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
