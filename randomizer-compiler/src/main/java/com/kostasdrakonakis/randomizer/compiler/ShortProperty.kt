package com.kostasdrakonakis.randomizer.compiler

import java.util.Random
import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class ShortProperty(element: Element) : RandomProperty(element) {
    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.SHORT
    }

    override val randomValue: String get() = "${Random().nextInt(1 shl 16).toShort()}"
}