package com.kostasdrakonakis.randomizer.compiler

import javax.lang.model.element.Element
import javax.lang.model.type.TypeKind
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class CharProperty(element: Element?) : RandomProperty(element!!) {
    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        return element.asType().kind == TypeKind.CHAR
    }

    override val randomValue: String get() = "$randomChar"

    private val randomChar: Char
        get() {
            val rnd = (Math.random() * 52).toInt()
            val base = if (rnd < 26) 'A' else 'a'
            return (base.code + rnd % 26).toChar()
        }
}
