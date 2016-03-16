package me.robin.xposed_wx_hook;

import de.robv.android.xposed.XposedBridge;

import java.lang.reflect.Method;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class ReflectionUtils {
    public static Object invokeMethod(Object target, String method, Object... params) {
        if (null == target) {
            return null;
        }
        try {
            Method m = target.getClass().getMethod(method);
            return m.invoke(target);
        } catch (Throwable r) {
            XposedBridge.log(r);
            return null;
        }
    }
}
