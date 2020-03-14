package com.kostasdrakonakis.randomizer

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.annotation.CheckResult
import androidx.annotation.UiThread
import androidx.fragment.app.Fragment
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.util.LinkedHashMap

object Randomizer {
    private val TAG = Randomizer::class.java.simpleName
    private var debug = false
    private const val RANDOM_CLASS_SUFFIX = "_RANDOM_PROPERTY_BINDING"
    private val CONSTRUCTOR_MAP: MutableMap<Class<*>, Constructor<*>?> = LinkedHashMap()

    @UiThread
    fun bind(activity: Activity) {
        createBinder(activity)
    }

    @UiThread
    fun bind(fragment: Fragment) {
        createBinder(fragment.requireContext())
    }

    @UiThread
    fun bind(context: Context) {
        createBinder(context)
    }

    fun setDebug(debug: Boolean) {
        this.debug = debug
    }

    private fun createBinder(context: Context) {
        val targetClass: Class<*> = context.javaClass
        try {
            val constructor = getConstructorForClass(targetClass)
            constructor?.newInstance(context)
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InstantiationException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }

    @CheckResult
    @UiThread
    private fun getConstructorForClass(cls: Class<*>): Constructor<*>? {
        var bindingConstructor = CONSTRUCTOR_MAP[cls]
        if (bindingConstructor != null) {
            if (debug) Log.d(TAG, "Returned constructor from Cache.")
            return bindingConstructor
        }
        val clsPackage = cls.getPackage() ?: return null
        val clsName = clsPackage.name + "." + cls.simpleName
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            if (debug) Log.d(TAG, "Reached framework class. No further.")
            return null
        }
        try {
            val classLoader = cls.classLoader ?: return null
            val bindingClass = classLoader.loadClass(clsName + RANDOM_CLASS_SUFFIX)
            if (debug) Log.d(TAG, "Binding Class: $bindingClass")
            bindingConstructor = bindingClass.getConstructor(cls)
            if (debug) Log.d(TAG, "Loaded binding class and constructor.")
        } catch (e: ClassNotFoundException) {
            return null
        } catch (e: NoSuchMethodException) {
            throw RuntimeException("Unable to find binding constructor for $clsName", e)
        }
        CONSTRUCTOR_MAP[cls] = bindingConstructor
        return bindingConstructor
    }
}