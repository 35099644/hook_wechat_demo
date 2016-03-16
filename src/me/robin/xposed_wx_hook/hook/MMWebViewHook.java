package me.robin.xposed_wx_hook.hook;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.robin.xposed_wx_hook.HookUtil;
import me.robin.xposed_wx_hook.Hooker;
import me.robin.xposed_wx_hook.InvocationPrinter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Lubin.Xuan on 2016/3/14.
 */
public class MMWebViewHook implements Hooker {

    private static final String CLAZZ = "com.tencent.mm.ui.widget.MMWebView";

    private Method superOverScrollBy;

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        Class<?> target = XposedHelpers.findClass(CLAZZ, lpparam.classLoader);

        XC_MethodHook.Unhook super_overScrollBy = HookUtil.tryHookSupper(
                target,
                "super_overScrollBy",
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                int.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        InvocationPrinter.printParam(param);
                    }
                }
        );

        if (null != super_overScrollBy) {
            superOverScrollBy = (Method) super_overScrollBy.getHookedMethod();
        }

        HookUtil.tryHookSupper(
                target,
                "super_onScrollChanged",
                int.class,
                int.class,
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        InvocationPrinter.printParam(param);
                    }
                }
        );

        HookUtil.tryHookSupper(
                target,
                "super_onOverScrolled",
                int.class,
                int.class,
                boolean.class,
                boolean.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        InvocationPrinter.printParam(param);
                    }
                }
        );
    }

    public void scrollToBottom(Object target) throws InvocationTargetException, IllegalAccessException {
        superOverScrollBy.invoke(target, 0, 0, 0, 18000, 0, 18000, 0, 0, false);
    }
}
