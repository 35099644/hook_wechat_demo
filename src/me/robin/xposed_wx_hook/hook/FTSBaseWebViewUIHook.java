package me.robin.xposed_wx_hook.hook;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import me.robin.xposed_wx_hook.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by Lubin.Xuan on 2016/3/8.
 */
public class FTSBaseWebViewUIHook implements Hooker {

    private static final String CLAZZ = "com.tencent.mm.plugin.webview.ui.tools.fts.FTSBaseWebViewUI";

    private Method searchMethod, inputSetMethod;

    private String defaultImgSrc;

    boolean searchAble = false;

    private MMWebViewHook mmWebViewHook = new MMWebViewHook();

    private int scrollPage = 0;

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        final ClassLoader classLoader = lpparam.classLoader;

        Class<?> target = XposedHelpers.findClass(CLAZZ, classLoader);

        try {
            searchMethod = XposedHelpers.findMethodExact(target, "kE", String.class);
        } catch (Throwable e) {
            XposedBridge.log("方法 kE 没有找到~~~~");
        }

        try {
            inputSetMethod = XposedHelpers.findMethodExact(target, "h", int.class, Bundle.class);
        } catch (Throwable e) {
            XposedBridge.log("方法 h 没有找到~~~~");
        }

        mmWebViewHook.handleLoadPackage(lpparam);

        HookUtil.tryHook(
                target,
                "h",
                int.class,
                Bundle.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        FTSBaseWebViewUIHandler.logData(param);

                        if (20 == (Integer) param.args[0]) {
                            Bundle bundle = (Bundle) param.args[1];
                            if (null == defaultImgSrc) {
                                defaultImgSrc = bundle.getString("fts_key_src");
                            } else {
                                bundle.putString("fts_key_src", defaultImgSrc);
                            }
                        }
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (19 == (Integer) param.args[0]) {
                            Bundle bundle = (Bundle) param.args[1];
                            String retJsonData = bundle.getString("fts_key_json_data");
                            HttpSendPool.addDataToQueue(retJsonData);
                            if (retJsonData.contains("\"continueFlag\":1") && scrollPage < 0) {
                                Field field = param.thisObject.getClass().getField("fHK");
                                Object webView = field.get(param.thisObject);
                                mmWebViewHook.scrollToBottom(webView);
                                scrollPage++;
                            } else {
                                searchNext((Activity) param.thisObject);
                            }
                        } else if (21 == (Integer) param.args[0] && searchAble) {
                            searchAble = false;
                            XposedBridge.log("进入公众号关键词搜索Activity,初始化完成,开始启动搜索");
                            searchNext((Activity) param.thisObject);
                        }
                    }
                }
        );

        HookUtil.tryHook(
                target,
                "Zz",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        Activity activity = (Activity) param.thisObject;
                        Intent intent = activity.getIntent();
                        int ftsType = intent.getIntExtra("ftsType", 0);
                        int ftsBizScene = intent.getIntExtra("ftsbizscene", 3);
                        boolean ftsNeedKeyboard = intent.getBooleanExtra("ftsneedkeyboard", false);
                        XposedBridge.log("ftsType:" + ftsType + "   ftsBizScene:" + ftsBizScene + "  ftsNeedKeyboard:" + ftsNeedKeyboard);
                        if (ftsType == 2 && ftsBizScene == 14) {
                            XposedBridge.log("进入公众号关键词搜索Activity,设置启动标记");
                            searchAble = true;
                        }
                        if (ftsNeedKeyboard) {
                            intent.putExtra("ftsneedkeyboard", false);
                        }
                    }
                }
        );
    }

    final String[] searchWords = new String[]{"诸暨", "杭州", "G20", "交通事故", "悲剧"};

    private int idx = 0;

    private void searchNext(final Activity activity) {

        scrollPage = 0;

        final String word = searchWords[idx % searchWords.length];
        idx++;
        XposedBridge.log("下一个查询词:" + word);

        new HandlerThread("query_next_word") {

            @Override
            public void run() {
                final Bundle bundle = new Bundle();
                bundle.putString("fts_key_new_query", word);
                bundle.putBoolean("fts_key_need_keyboard", false);
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            inputSetMethod.invoke(activity, 22, bundle);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        if (null != searchMethod) {
                            try {
                                searchMethod.invoke(activity, word);
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        }.start();
    }
}
