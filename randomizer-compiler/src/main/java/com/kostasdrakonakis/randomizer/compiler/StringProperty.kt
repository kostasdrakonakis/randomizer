package com.kostasdrakonakis.randomizer.compiler

import com.kostasdrakonakis.randomizer.annotations.RandomString
import java.util.Random
import java.util.UUID
import javax.lang.model.element.Element
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

internal class StringProperty(element: Element) : RandomProperty(element) {
    private val uuid: Boolean = element.getAnnotation(RandomString::class.java).uuid

    override fun isTypeValid(elements: Elements, types: Types): Boolean {
        val elementType = element.asType()
        val string = elements.getTypeElement(QUALIFIER_STRING).asType()
        return types.isSameType(elementType, string)
    }

    override val randomValue: String
        get() = if (uuid) {
            "\"${UUID.randomUUID()}\""
        } else {
            val builder = StringBuilder()
            val random = Random()
            for (i in 0..19) {
                val randIndex = random.nextInt(SEED.length)
                builder.append(SEED[randIndex])
            }
            "\"$builder\""
        }

    companion object {
        private const val QUALIFIER_STRING = "java.lang.String"
        private const val SEED = "ABCDEFGHJKLMNOPRSTUVYZabcdefghjklmnoprstuvyz"
    }

}