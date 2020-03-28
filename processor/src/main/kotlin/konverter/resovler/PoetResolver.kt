package konverter.resovler

import com.squareup.kotlinpoet.FunSpec
import konverter.domain.KonvertMetaInfo
import konverter.domain.KonvertPoetInfo
import java.lang.String.format
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

object PoetResolver {

    fun buildKonvertFunction(meta: KonvertMetaInfo): KonvertPoetInfo {
        val fromClass = meta.fromClass
        val toClass = meta.toClass

        val membersMap = MetaResolver.resolveMembers(meta)

        val resolvedMembersMap = ContractResolver.apply(
            meta = meta,
            membersMap = membersMap
        )

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
}