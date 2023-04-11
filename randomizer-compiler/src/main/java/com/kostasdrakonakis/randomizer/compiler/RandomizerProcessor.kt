package com.kostasdrakonakis.randomizer.compiler

import com.google.auto.service.AutoService
import com.kostasdrakonakis.randomizer.annotations.RandomChar
import com.kostasdrakonakis.randomizer.annotations.RandomDouble
import com.kostasdrakonakis.randomizer.annotations.RandomFloat
import com.kostasdrakonakis.randomizer.annotations.RandomInt
import com.kostasdrakonakis.randomizer.annotations.RandomLong
import com.kostasdrakonakis.randomizer.annotations.RandomShort
import com.kostasdrakonakis.randomizer.annotations.RandomString
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType
import java.io.IOException
import java.util.ArrayList
import java.util.LinkedHashMap
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.FilerException
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.ElementKind
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@IncrementalAnnotationProcessor(IncrementalAnnotationProcessorType.AGGREGATING)
@AutoService(Processor::class)
class RandomizerProcessor : AbstractProcessor() {

    private lateinit var filer: Filer
    private lateinit var messager: Messager
    private lateinit var elements: Elements
    private lateinit var types: Types

    @Synchronized
    override fun init(processingEnvironment: ProcessingEnvironment) {
        super.init(processingEnvironment)
        filer = processingEnvironment.filer
        messager = processingEnvironment.messager
        elements = processingEnvironment.elementUtils
        types = processingEnvironment.typeUtils
    }

    override fun process(annotations: Set<TypeElement?>, roundEnv: RoundEnvironment): Boolean {
        if (parseRandomPropertyElements(roundEnv)) return true
        if (roundEnv.processingOver()) {
            try {
                val navigatorClass = TypeSpec
                    .classBuilder(Constants.GENERATED_CLASS_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                val constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addStatement("throw new UnsupportedOperationException(\"No instances\")")
                    .build()
                navigatorClass.addMethod(constructor)
                JavaFile.builder(Constants.PACKAGE_NAME, navigatorClass.build())
                    .build()
                    .writeTo(filer)
            } catch (_: FilerException) {
            } catch (_: IOException) {
            }
        }
        return false
    }

    override fun getSupportedAnnotationTypes(): Set<String> {
        val supportedAnnotationTypes: MutableSet<String> = linkedSetOf()
        for (annotation in supportedAnnotations) {
            supportedAnnotationTypes.add(annotation.canonicalName)
        }
        return supportedAnnotationTypes
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    private val supportedAnnotations: Set<Class<out Annotation?>>
        get() = linkedSetOf(
            RandomInt::class.java,
            RandomString::class.java,
            RandomDouble::class.java,
            RandomFloat::class.java,
            RandomShort::class.java,
            RandomLong::class.java,
            RandomChar::class.java
        )

    private fun parseRandomPropertyElements(roundEnv: RoundEnvironment): Boolean {
        val annotatedElementMap: MutableMap<String, MutableList<RandomProperty>?> = LinkedHashMap()
        for (annotationClass in supportedAnnotations) {
            val elementSet = roundEnv.getElementsAnnotatedWith(annotationClass)
            if (elementSet.isEmpty()) continue
            for (element in elementSet) {
                if (element.kind != ElementKind.FIELD) {
                    printError("@%s must be applied to fields.", annotationClass.canonicalName)
                    return true
                }
                val modifiers = element.modifiers
                if (modifiers.contains(Modifier.PRIVATE)
                    || modifiers.contains(Modifier.FINAL)
                    || modifiers.contains(Modifier.NATIVE)) {
                    printError("@%s must be applied to public, protected, package-private fields only.", annotationClass.simpleName)
                    return true
                }
                val annotation = element.getAnnotation(annotationClass)!!
                val annotationName: String = annotation::class.java.simpleName
                when (annotation) {
                    is RandomInt -> {
                        val property = IntProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to int", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomString -> {
                        val property = StringProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to String", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomDouble -> {
                        val property = DoubleProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to double", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomFloat -> {
                        val property = FloatProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to float", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomShort -> {
                        val property = ShortProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to short", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomLong -> {
                        val property = LongProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to long", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    is RandomChar -> {
                        val property = CharProperty(element)
                        if (!property.isTypeValid(elements, types)) {
                            printError("@%s must be applied to String", annotationName)
                        }
                        addElement(annotatedElementMap, property)
                    }
                    else -> {
                        printError("Annotation: " + annotation::class.java.simpleName + " is not supported")
                    }
                }
            }
        }
        if (annotatedElementMap.isEmpty()) return true
        try {
            for ((key, value) in annotatedElementMap) {
                val constructor = createConstructor(value)
                val binder = createClass(getClassName(key), constructor)
                val javaFile = JavaFile.builder(getPackage(key), binder).build()
                javaFile.writeTo(filer)
            }
        } catch (e: IOException) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file")
        }
        return true
    }

    private fun createConstructor(randomElements: List<RandomProperty>?): MethodSpec {
        val firstElement = randomElements!![0]
        val builder = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(TypeName.get(firstElement.element.enclosingElement.asType()), Constants.CONST_PARAM_TARGET_NAME)
        for (i in randomElements.indices) {
            addStatement(builder, randomElements[i])
        }
        return builder.build()
    }

    private fun printError(message: String, format: String? = null) {
        messager.printMessage(Diagnostic.Kind.ERROR, if (format != null) String.format(message, format) else message)
    }

    private fun addStatement(builder: MethodSpec.Builder, randomElement: RandomProperty) {
        builder.addStatement(String.format(
            Constants.TARGET_STATEMENT_FORMAT,
            randomElement.elementName.toString(),
            randomElement.randomValue))
    }

    private fun createClass(className: String, constructor: MethodSpec): TypeSpec {
        return TypeSpec.classBuilder(className + Constants.RANDOM_CLASS_SUFFIX)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(constructor)
            .build()
    }

    private fun getPackage(qualifier: String): String {
        return qualifier.substring(0, qualifier.lastIndexOf(Constants.CHAR_DOT))
    }

    private fun getClassName(qualifier: String): String {
        return qualifier.substring(qualifier.lastIndexOf(Constants.CHAR_DOT) + 1)
    }

    private fun addElement(map: MutableMap<String, MutableList<RandomProperty>?>, randomElement: RandomProperty) {
        val qualifier = randomElement.qualifiedClassName.toString()
        if (map[qualifier] == null) {
            map[qualifier] = ArrayList()
        }
        map[qualifier]!!.add(randomElement)
    }
}