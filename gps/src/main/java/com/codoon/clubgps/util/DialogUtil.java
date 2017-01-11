package com.codoon.clubgps.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PixelFormat;
import android.support.v7.app.AlertDialog;
import android.view.WindowManager;

import com.codoon.clubgps.R;


/**
 * Created by Frankie on 2016/9/7.
 * <p>
 * 创建各种样式dialog的工具
 */
public class DialogUtil {

    private Context mContext;

    public DialogUtil(Context context) {
        this.mContext = context;
    }

    public Dialog createAlertDialog(String message){
        return createAlertDialog(message, null, null);
    }

    /**
     * 创建一个提示框
     * @param message
     * @param positiveClickListener
     * @return
     */
    public Dialog createAlertDialog(String message, DialogInterface.OnClickListener positiveClickListener){
        return createAlertDialog(message, positiveClickListener, null);
    }

    public Dialog createAlertDialog(String message, DialogInterface.OnClickListener positiveClickListener, DialogInterface.OnClickListener negativeClickListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.dialog);
        builder.setTitle(R.string.alert);
        builder.setMessage(message);

        builder.setPositiveButton(R.string.confirm, positiveClickListener);

        if(negativeClickListener != null)
            builder.setNegativeButton(R.string.cancel, negativeClickListener);

        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = CommonUtil.dip2px(300);
        params.format = PixelFormat.TRANSLUCENT;
        dialog.getWindow().setAttributes(params);

        return dialog;
    }

}
