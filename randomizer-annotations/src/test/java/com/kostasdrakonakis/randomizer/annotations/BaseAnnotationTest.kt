package com.kostasdrakonakis.randomizer.annotations

import com.google.common.reflect.ClassPath
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
abstract class BaseAnnotationTest {

    @Suppress("UnstableApiUsage")
    protected inline fun <reified T : Annotation> annotations(): List<Class<*>> {
        return ClassPath.from(T::class.java.classLoader)
            .allClasses
            .filter { it.name.startsWith(ANNOTATIONS_PACKAGE) }
            .map { it.load() }
            .filter { it.isAnnotation }
    }

    @Suppress("UnstableApiUsage")
    protected inline fun <reified T : Annotation> annotation(): String {
        return T::class.java.canonicalName
    }

    @Suppress("UnstableApiUsage")
    protected inline fun <reified T : Annotation> Class<*>.annotation(): T? {
        return this.getAnnotation(T::class.java)
    }

    companion object {
        const val ANNOTATIONS_PACKAGE = "com.kostasdrakonakis.randomizer.annotations"
    }
}