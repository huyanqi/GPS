package com.codoon.clubgps.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.codoon.clubgps.R;
import com.codoon.clubgps.application.GPSApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Frankie on 16/11/30.
 */

public class PermissionUtils {

    private Activity mActivity;

    private PermissionUtils(){}

    public PermissionUtils(Activity activity){
        this.mActivity = activity;
    }

    /**
     * 检测所有的权限是否都已授权
     *
     * @param permissions
     * @return
     */
    public boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private boolean checkPermission(String permission){
        if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    /**
     * 过滤出没有授权的权限
     *
     * @param permissions
     * @return
     */
    public List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(mActivity, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * 请求授权
     * @param permissions
     * @param request_code
     */
    public void requestPermissions(String[] permissions, int request_code){
        List<String> ps = getDeniedPermissions(permissions);
        ActivityCompat.requestPermissions(mActivity, ps.toArray(new String[ps.size()]), request_code);
    }

    /**
     * 弹出重新授权的提示框
     */
    public void showTips(final int request_code){

        new DialogUtil(mActivity).createAlertDialog(GPSApplication.getContext().getString(R.string.permisson_rejected), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startAppSettings(request_code);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mActivity.finish();
            }
        }).show();
    }

    private void startAppSettings(int request_code) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
        mActivity.startActivityForResult(intent, request_code);
    }

}
