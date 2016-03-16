package me.robin.xposed_wx_hook;

import de.robv.android.xposed.XposedBridge;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class X5WebViewAdapterPrint {

    private static final String name = "com.tencent.smtt.webkit.adapter.X5WebViewAdapter";

    public static void print(Object target) {
        if (null != target && target.getClass().getName().equals(name)) {
            Object ourl = ReflectionUtils.invokeMethod(target, "getOriginalUrl");
            Object url = ReflectionUtils.invokeMethod(target, "getUrl");
            InvocationPrinter.print(target);
            XposedBridge.log("\n " + ourl + " " + url);
        }
    }
}
