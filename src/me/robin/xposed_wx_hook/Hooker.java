package me.robin.xposed_wx_hook;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by Lubin.Xuan on 2016/3/14.
 */
public interface Hooker {
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable;
}
