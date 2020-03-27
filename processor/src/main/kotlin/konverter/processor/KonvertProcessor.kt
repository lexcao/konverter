package konverter.processor

import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import konverter.annotation.Konvert
import konverter.domain.KonvertMetaInfo
import konverter.helper.filer
import konverter.helper.initTools
import konverter.resovler.ContractResolver
import konverter.resovler.MetaResolver
import java.lang.String.format
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.annotation.processing.SupportedSourceVersion
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.TypeElement

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("konverter.annotation.Konvert")
class KonvertProcessor : AbstractProcessor() {

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        initTools(processingEnv)
    }

    override fun process(
        annotations: MutableSet<out TypeElement>,
        roundEnv: RoundEnvironment
    ): Boolean {
        if (annotations.isEmpty()) return true

        roundEnv.getElementsAnnotatedWith(Konvert::class.java)
            .filter { it.kind == ElementKind.CLASS }
            .filterIsInstance<TypeElement>()
            .map(MetaResolver::resolveKonvert)
            .let(::process)

        return true
    }

    private fun process(them: List<KonvertMetaInfo>) {
        if (them.isEmpty()) return
        val fileName = "konvert-generated"
        val packageName = them.first().packageElement.toString()

        val fileBuilder = FileSpec.builder(packageName, fileName)

        fileBuilder.addImport("java.time", "ZoneOffset")

        them.map(::process).forEach {
            fileBuilder.addFunction(it)
        }

        fileBuilder.build().writeTo(filer)
    }

    private fun process(meta: KonvertMetaInfo): FunSpec {

        val fromClass = meta.fromClass
        val toClass = meta.toClass


        val membersMap = MetaResolver.resolveMembers(meta)

        val resolvedMembersMap = ContractResolver.apply(
            contract = meta.contract,
            membersMap = membersMap
        )

        val members = toClass.members.joinToString(", ") {
            format("%s = %s", it.simpleName, resolvedMembersMap[it])
        }

        val funSpec = FunSpec.builder("to" + toClass.name.simpleName)
            .addKdoc("Auto generated code by @Konvert annotation processor")
            .receiver(fromClass.type)
            .addStatement("return %T($members)", toClass.type)
            .returns(toClass.type)
            .build()

        return funSpec
    }
}