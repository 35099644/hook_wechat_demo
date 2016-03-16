package me.robin.xposed_wx_hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

import java.lang.reflect.Method;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class InvocationPrinter {
    public static void print(Object target) {

        if (null == target) {
            return;
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("类:").append(target.getClass()).append("\n");
        XposedBridge.log(stringBuilder.toString());
        stringBuilder.setLength(0);
        Method[] methods = target.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            Class[] pt = method.getParameterTypes();
            stringBuilder.append("\n\tmember:").append(methods[i].getName());
            for (int j = 0; j < pt.length; j++) {
                stringBuilder.append("\n\t\t").append(pt[j].getName()).append(",");
            }
            XposedBridge.log(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
    }

    public static void printParam(XC_MethodHook.MethodHookParam param) {
        Object[] args = param.args;
        StringBuilder paramStr = new StringBuilder();
        paramStr.append(param.thisObject).append("  #").append(param.method.getName());
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            paramStr.append("arg").append(i).append(":").append(arg);
        }

        XposedBridge.log(paramStr.toString());

    }


    public static void printCallStack() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        StringBuilder stringBuilder = new StringBuilder("调用栈:");
        for (int i = 0; i < stackTraceElements.length - 2; i++) {
            if (i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append("\t").append(i);
            for (int j = 0; j < i; j++) {
                stringBuilder.append("\t");
            }
            StackTraceElement stack = stackTraceElements[i + 2];
            stringBuilder.append(stack.getClassName()).append(".").append(stack.getMethodName()).append(" (").append(stack.getLineNumber());
            stringBuilder.append(" at ").append(stack.getFileName());
        }
        XposedBridge.log(stringBuilder.toString());
    }
}
