package com.kostasdrakonakis.randomizer.compiler

import com.kostasdrakonakis.randomizer.annotations.RandomFloat
import java.util.Random
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class FloatProperty(element: Element) : RandomProperty(element) {
    private val minValue: Float = element.getAnnotation(RandomFloat::class.java).minValue
    private val maxValue: Float = element.getAnnotation(RandomFloat::class.java).maxValue

    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.FLOAT
    }

    override val randomValue: String
        get() = "${minValue + Random().nextFloat() * (maxValue - minValue)}"
}
