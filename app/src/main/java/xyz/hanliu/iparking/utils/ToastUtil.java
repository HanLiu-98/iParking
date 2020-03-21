package xyz.hanliu.iparking.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 弹出提示的工具类
 */


public class ToastUtil {

    public static Toast mToast;

    public static void showMsg(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
