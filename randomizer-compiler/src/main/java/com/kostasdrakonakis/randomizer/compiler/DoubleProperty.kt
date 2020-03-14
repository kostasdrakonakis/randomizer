package com.kostasdrakonakis.randomizer.compiler

import com.kostasdrakonakis.randomizer.annotations.RandomDouble
import java.util.Random
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class DoubleProperty(element: Element) : RandomProperty(element) {
    private val minValue: Double = element.getAnnotation(RandomDouble::class.java).minValue
    private val maxValue: Double = element.getAnnotation(RandomDouble::class.java).maxValue

    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.DOUBLE
    }

    override val randomValue: String
        get() = "${minValue + Random().nextDouble() * (maxValue - minValue)}"

}