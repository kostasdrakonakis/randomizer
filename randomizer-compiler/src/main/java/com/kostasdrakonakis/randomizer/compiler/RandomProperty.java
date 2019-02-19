package com.kostasdrakonakis.randomizer.compiler;

import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

abstract class RandomProperty {
    Element element;
    Name qualifiedClassName;
    Name simpleClassName;
    Name elementName;
    TypeMirror elementType;

    public RandomProperty(Element element) {
        this.element = element;
        elementName = element.getSimpleName();
        simpleClassName = element.getEnclosingElement().getSimpleName();
        TypeElement enclosingElement = ((TypeElement) element.getEnclosingElement());
        qualifiedClassName = enclosingElement.getQualifiedName();
        elementType = element.asType();
    }

    Name getQualifiedClassName() {
        return qualifiedClassName;
    }

    Name getSimpleClassName() {
        return simpleClassName;
    }

    Name getElementName() {
        return elementName;
    }

    TypeMirror getElementType() {
        return elementType;
    }

    Element getElement() {
        return element;
    }

    abstract boolean isTypeValid(Elements elements, Types types);

    abstract String getRandomValue();
}
