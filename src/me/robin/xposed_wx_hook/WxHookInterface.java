package me.robin.xposed_wx_hook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.robin.xposed_wx_hook.hook.FTSBaseWebViewUIHook;
import me.robin.xposed_wx_hook.hook.MMWebViewHook;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class WxHookInterface implements IXposedHookLoadPackage {

    private List<Hooker> hookerList = new ArrayList<Hooker>();

    public WxHookInterface() {
        hookerList.add(new FTSBaseWebViewUIHook());
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {


        if (!lpparam.packageName.equals("com.tencent.mm"))
            return;

        XposedBridge.log("try Hook wx:" + lpparam.packageName);

        for (Hooker hooker : hookerList) {
            hooker.handleLoadPackage(lpparam);
        }
    }

}
