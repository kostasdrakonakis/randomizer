package com.kostasdrakonakis.randomizer.compiler

import javax.lang.model.element.Element
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal abstract class RandomProperty(var element: Element) {
    val qualifiedClassName: Name
    val simpleClassName: Name = element.enclosingElement.simpleName
    val elementName: Name = element.simpleName
    private val elementType: TypeMirror

    abstract fun isTypeValid(elements: Elements, types: Types): Boolean
    abstract val randomValue: String

    init {
        val enclosingElement = element.enclosingElement as TypeElement
        qualifiedClassName = enclosingElement.qualifiedName
        elementType = element.asType()
    }
}