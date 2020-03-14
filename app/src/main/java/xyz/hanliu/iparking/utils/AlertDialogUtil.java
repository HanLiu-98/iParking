package xyz.hanliu.iparking.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import xyz.hanliu.iparking.R;

public class AlertDialogUtil {

    public static void showNetErrorAlertDialog(Context context, String errormsg) {
        String msg = "错误信息:\n" + errormsg + "\n请重新尝试！";
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle("网络请求失败").
                setIcon(R.drawable.ic_waring_red).
                setMessage(msg);
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    public static void showFailAlertDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle("操作失败").
                setIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp).
                setMessage(msg);
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    public static void showDataInvaidAlertDialog(Context context, String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).
                setTitle("输入数据不合法").
                setIcon(R.drawable.ic_icon_waring).
                setMessage(msg);
        builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }


}
