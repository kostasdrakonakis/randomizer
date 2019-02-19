package com.kostasdrakonakis.randomizer;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Randomizer {

    private static final String TAG = Randomizer.class.getSimpleName();
    private static boolean debug;

    private static final String RANDOM_CLASS_SUFFIX = "_RANDOM_PROPERTY_BINDING";
    private static final Map<Class<?>, Constructor> CONSTRUCTOR_MAP = new LinkedHashMap<>();

    private Randomizer() {
        throw new UnsupportedOperationException("No instances allowed");
    }

    @UiThread
    public static void bind(@NonNull Activity activity) {
        createBinder(activity);
    }

    @UiThread
    public static void bind(@NonNull Fragment fragment) {
        createBinder(fragment.requireContext());
    }

    @UiThread
    public static void bind(@NonNull Context context) {
        createBinder(context);
    }

    public static void setDebug(boolean debug) {
        Randomizer.debug = debug;
    }

    private static void createBinder(@NonNull Context context) {
        Class<?> targetClass = context.getClass();
        try {
            Constructor constructor = getConstructorForClass(targetClass);
            if (constructor != null) {
                constructor.newInstance(context);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @CheckResult
    @UiThread
    private static Constructor getConstructorForClass(Class<?> cls) {
        Constructor<?> bindingConstructor = CONSTRUCTOR_MAP.get(cls);
        if (bindingConstructor != null) {
            if (debug) Log.d(TAG, "Returned constructor from Cache.");
            return bindingConstructor;
        }

        Package clsPackage = cls.getPackage();
        if (clsPackage == null) return null;

        String clsName = clsPackage.getName() + "." + cls.getSimpleName();
        if (clsName.startsWith("android.") || clsName.startsWith("java.")) {
            if (debug) Log.d(TAG, "Reached framework class. No further.");
            return null;
        }

        try {
            ClassLoader classLoader = cls.getClassLoader();
            if (classLoader == null) return null;

            Class<?> bindingClass = classLoader.loadClass(clsName + RANDOM_CLASS_SUFFIX);
            if (debug) Log.d(TAG, "Binding Class: " + bindingClass);

            //noinspection unchecked
            bindingConstructor = bindingClass.getConstructor(cls);
            if (debug) Log.d(TAG, "Loaded binding class and constructor.");
        } catch (ClassNotFoundException e) {
            return null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Unable to find binding constructor for " + clsName, e);
        }
        CONSTRUCTOR_MAP.put(cls, bindingConstructor);
        return bindingConstructor;
    }

}
