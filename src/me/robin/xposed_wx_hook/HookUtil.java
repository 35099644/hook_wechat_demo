package me.robin.xposed_wx_hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

import java.lang.reflect.Method;

/**
 * Created by Lubin.Xuan on 2016/3/14.
 */
public class HookUtil extends XposedHelpers {
    public static XC_MethodHook.Unhook tryHook(ClassLoader classLoader, String className, String methodName, Object... parameterTypesAndCallback) {

        try {
            Class<?> target = findClass(className, classLoader);
            return tryHook(target, methodName, parameterTypesAndCallback);
        } catch (Throwable r) {
            XposedBridge.log("Error When Hook WX " + r.toString());
            return null;
        }
    }

    public static XC_MethodHook.Unhook tryHook(Class<?> target, String methodName, Object... parameterTypesAndCallback) {

        try {
            XposedBridge.log("HookClass:" + target + "   method:" + methodName);

            XC_MethodHook.Unhook unhook = findAndHookMethod(
                    target,
                    methodName,
                    parameterTypesAndCallback
            );
            XposedBridge.log("HookInfo:" + unhook.getHookedMethod());
            return unhook;
        } catch (Throwable r) {
            XposedBridge.log("Error When Hook WX " + r.toString());
            return null;
        }
    }

    public static XC_MethodHook.Unhook tryHookSupper(ClassLoader classLoader, String className, String methodName, Object... parameterTypesAndCallback) {

        try {
            Class<?> clazz = findClass(className, classLoader);

            return tryHookSupper(clazz, methodName, parameterTypesAndCallback);
        } catch (Throwable r) {
            XposedBridge.log("Error When Hook WX " + r.toString());
            return null;
        }
    }

    public static XC_MethodHook.Unhook tryHookSupper(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {

        try {
            XposedBridge.log("HookClass:" + clazz);

            Method method = clazz.getMethod(methodName, getParameterClasses(clazz.getClassLoader(), parameterTypesAndCallback));

            XC_MethodHook.Unhook unhook = XposedBridge.hookMethod(method, (XC_MethodHook) parameterTypesAndCallback[parameterTypesAndCallback.length - 1]);

            XposedBridge.log("HookInfo:" + unhook.getHookedMethod());
            return unhook;
        } catch (Throwable r) {
            XposedBridge.log("Error When Hook WX " + r.toString());
            return null;
        }
    }


    private static Class<?>[] getParameterClasses(ClassLoader classLoader, Object[] parameterTypesAndCallback) {
        Class<?>[] parameterClasses = null;
        for (int i = parameterTypesAndCallback.length - 1; i >= 0; i--) {
            Object type = parameterTypesAndCallback[i];
            if (type == null)
                throw new ClassNotFoundError("parameter type must not be null", null);

            // ignore trailing callback
            if (type instanceof XC_MethodHook)
                continue;

            if (parameterClasses == null)
                parameterClasses = new Class<?>[i + 1];

            if (type instanceof Class)
                parameterClasses[i] = (Class<?>) type;
            else if (type instanceof String)
                parameterClasses[i] = findClass((String) type, classLoader);
            else
                throw new ClassNotFoundError("parameter type must either be specified as Class or String", null);
        }

        // if there are no arguments for the method
        if (parameterClasses == null)
            parameterClasses = new Class<?>[0];

        return parameterClasses;
    }
}
