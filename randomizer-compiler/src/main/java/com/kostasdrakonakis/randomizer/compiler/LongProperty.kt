package com.kostasdrakonakis.randomizer.compiler

import com.kostasdrakonakis.randomizer.annotations.RandomLong
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class LongProperty(element: Element) : RandomProperty(element) {

    private val minValue: Long = element.getAnnotation(RandomLong::class.java).minValue
    private val maxValue: Long = element.getAnnotation(RandomLong::class.java).maxValue

    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.LONG
    }

    override val randomValue: String
        get() = "${minValue + (Math.random() * (maxValue - minValue)).toLong()}"
}
