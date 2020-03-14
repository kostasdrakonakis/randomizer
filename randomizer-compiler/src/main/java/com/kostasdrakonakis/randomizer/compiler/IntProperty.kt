package com.kostasdrakonakis.randomizer.compiler

import com.kostasdrakonakis.randomizer.annotations.RandomInt
import java.util.Random
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class IntProperty(element: Element) : RandomProperty(element) {
    private val minValue: Int = element.getAnnotation(RandomInt::class.java).minValue
    private val maxValue: Int = element.getAnnotation(RandomInt::class.java).maxValue

    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.INT
    }

    override val randomValue: String
        get() = "${(minValue + Random().nextInt(maxValue - minValue + 1))}"
}
