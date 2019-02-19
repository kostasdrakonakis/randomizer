package com.kostasdrakonakis.randomizer.compiler;

import com.kostasdrakonakis.randomizer.annotations.RandomChar;
import com.kostasdrakonakis.randomizer.annotations.RandomDouble;
import com.kostasdrakonakis.randomizer.annotations.RandomFloat;
import com.kostasdrakonakis.randomizer.annotations.RandomInt;
import com.kostasdrakonakis.randomizer.annotations.RandomLong;
import com.kostasdrakonakis.randomizer.annotations.RandomShort;
import com.kostasdrakonakis.randomizer.annotations.RandomString;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.kostasdrakonakis.randomizer.compiler.Constants.CHAR_DOT;
import static com.kostasdrakonakis.randomizer.compiler.Constants.CONST_PARAM_TARGET_NAME;
import static com.kostasdrakonakis.randomizer.compiler.Constants.GENERATED_CLASS_NAME;
import static com.kostasdrakonakis.randomizer.compiler.Constants.PACKAGE_NAME;
import static com.kostasdrakonakis.randomizer.compiler.Constants.RANDOM_CLASS_SUFFIX;
import static com.kostasdrakonakis.randomizer.compiler.Constants.TARGET_STATEMENT_FORMAT;

public class RandomizerProcessor extends AbstractProcessor {

    private Filer filer;
    private Messager messager;
    private Elements elements;
    private Types types;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        messager = processingEnvironment.getMessager();
        elements = processingEnvironment.getElementUtils();
        types = processingEnvironment.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try {

            if (parseRandomPropertyElements(roundEnv)) return true;

            TypeSpec.Builder navigatorClass = TypeSpec
                    .classBuilder(GENERATED_CLASS_NAME)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            MethodSpec constructor = MethodSpec.constructorBuilder()
                    .addModifiers(Modifier.PRIVATE)
                    .addStatement("throw new UnsupportedOperationException(\"No instances\")")
                    .build();

            navigatorClass.addMethod(constructor);

            JavaFile.builder(PACKAGE_NAME, navigatorClass.build()).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        for (Class<? extends Annotation> annotation : getSupportedAnnotations()) {
            types.add(annotation.getCanonicalName());
        }
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private Set<Class<? extends Annotation>> getSupportedAnnotations() {
        Set<Class<? extends Annotation>> annotations = new LinkedHashSet<>();
        annotations.add(RandomInt.class);
        annotations.add(RandomString.class);
        annotations.add(RandomDouble.class);
        annotations.add(RandomFloat.class);
        annotations.add(RandomShort.class);
        annotations.add(RandomLong.class);
        annotations.add(RandomChar.class);
        return annotations;
    }

    private boolean parseRandomPropertyElements(RoundEnvironment roundEnv) {
        Map<String, List<RandomProperty>> annotatedElementMap = new LinkedHashMap<>();

        for (Class<? extends Annotation> annotationClass : getSupportedAnnotations()) {
            Set<? extends Element> elementSet = roundEnv.getElementsAnnotatedWith(annotationClass);
            if (elements == null || elementSet.isEmpty()) continue;

            for (Element element : elementSet) {
                if (element.getKind() != ElementKind.FIELD) {
                    printError("@%s must be applied to fields.", annotationClass.getCanonicalName());
                    return true;
                }

                Set<Modifier> modifiers = element.getModifiers();
                if (modifiers.contains(Modifier.PRIVATE)
                        || modifiers.contains(Modifier.FINAL)
                        || modifiers.contains(Modifier.NATIVE)) {

                    printError("@%s must be applied to public, protected, package-private fields only.", annotationClass.getSimpleName());
                    return true;
                }

                Annotation annotation = element.getAnnotation(annotationClass);
                String annotationName = annotation.annotationType().getSimpleName();

                if (annotation instanceof RandomInt) {
                    IntProperty property = new IntProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to int", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomString) {
                    StringProperty property = new StringProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to String", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomDouble) {
                    DoubleProperty property = new DoubleProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to double", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomFloat) {
                    FloatProperty property = new FloatProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to float", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomShort) {
                    ShortProperty property = new ShortProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to short", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomLong) {
                    LongProperty property = new LongProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to long", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else if (annotation instanceof RandomChar) {
                    CharProperty property = new CharProperty(element);
                    if (!property.isTypeValid(elements, types)) {
                        printError("@%s must be applied to String", annotationName);
                    }
                    addElement(annotatedElementMap, property);
                } else {
                    printError("Annotation: " + annotation.annotationType() + " is not supported");
                }
            }
        }

        if (annotatedElementMap.isEmpty()) return true;

        try {
            for (Map.Entry<String, List<RandomProperty>> entry : annotatedElementMap.entrySet()) {
                MethodSpec constructor = createConstructor(entry.getValue());
                TypeSpec binder = createClass(getClassName(entry.getKey()), constructor);
                JavaFile javaFile = JavaFile.builder(getPackage(entry.getKey()), binder).build();
                javaFile.writeTo(filer);
            }
        } catch (IOException e) {
            messager.printMessage(Diagnostic.Kind.ERROR, "Error on creating java file");
        }

        return true;
    }

    private MethodSpec createConstructor(List<RandomProperty> randomElements) {
        RandomProperty firstElement = randomElements.get(0);
        MethodSpec.Builder builder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(firstElement.getElement().getEnclosingElement().asType()), CONST_PARAM_TARGET_NAME);
        for (int i = 0; i < randomElements.size(); i++) {
            addStatement(builder, randomElements.get(i));
        }
        return builder.build();
    }

    private void printError(String message) {
        printError(message, null);
    }

    private void printError(String message, String format) {
        messager.printMessage(Diagnostic.Kind.ERROR, format != null
                ? String.format(message, format)
                : message);
    }

    private void addStatement(MethodSpec.Builder builder, RandomProperty randomElement) {
        builder.addStatement(String.format(
                TARGET_STATEMENT_FORMAT,
                randomElement.getElementName().toString(),
                randomElement.getRandomValue())
        );
    }

    private TypeSpec createClass(String className, MethodSpec constructor) {
        return TypeSpec.classBuilder(className + RANDOM_CLASS_SUFFIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(constructor)
                .build();
    }

    private String getPackage(String qualifier) {
        return qualifier.substring(0, qualifier.lastIndexOf(CHAR_DOT));
    }

    private String getClassName(String qualifier) {
        return qualifier.substring(qualifier.lastIndexOf(CHAR_DOT) + 1);
    }

    private void addElement(Map<String, List<RandomProperty>> map, RandomProperty randomElement) {
        String qualifier = randomElement.getQualifiedClassName().toString();
        if (map.get(qualifier) == null) {
            map.put(qualifier, new ArrayList<>());
        }
        map.get(qualifier).add(randomElement);
    }
}
