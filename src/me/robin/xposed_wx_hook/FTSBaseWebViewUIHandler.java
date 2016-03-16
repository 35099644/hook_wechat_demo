package me.robin.xposed_wx_hook;

import android.os.Bundle;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;

/**
 * Created by Lubin.Xuan on 2016/3/9.
 */
public class FTSBaseWebViewUIHandler {
    public static void handle(int type, Bundle bundle, String... keys) {
        if (null == bundle || null == keys || keys.length == 0) {
            return;
        }


        StringBuilder paramData = new StringBuilder("FTSBaseWebViewUIHandler Handle:");

        paramData.append(type).append("\nBundleData");
        for (int i = 0; i < keys.length; i++) {
            paramData.append("\n\t").append(keys[i]).append(":");
            paramData.append(bundle.get(keys[i]));
        }

        XposedBridge.log(paramData.toString());
    }


    public static void logData(XC_MethodHook.MethodHookParam param) {
        int type = (Integer) param.args[0];
        Bundle bundle = (Bundle) param.args[1];
        switch (type) {
            case 19:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_json_data",
                        "fts_key_new_query"
                );
                break;
            case 20:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_ret",
                        "fts_key_id",
                        "fts_key_src"
                );
                break;
            case 21:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_json_data",
                        "fts_key_teach_request_type",
                        "fts_key_is_cache_data"
                );
                break;
            case 22:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_new_query",
                        "fts_key_need_keyboard"
                );
                break;
            case 24:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_json_data"
                );
                break;
            case 25:
                FTSBaseWebViewUIHandler.handle(
                        type,
                        bundle,
                        "fts_key_sns_id",
                        "fts_key_status"
                );
                break;
            default:
                break;
        }
    }
}
