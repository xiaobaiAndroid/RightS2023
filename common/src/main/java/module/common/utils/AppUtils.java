package module.common.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import module.common.type.AppPackageType;

/**
* @description:
* @createDate: 2023/6/5
*/
public class AppUtils {


    public static synchronized String getDeviceId(Context context){
        String deviceId ="";
        try {
            deviceId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceId;
    }

    public static boolean hasOldFMApp(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            List<ApplicationInfo> packageList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

            for (ApplicationInfo appInfo : packageList) {
                String appName = appInfo.loadLabel(packageManager).toString();
                String packageName = appInfo.packageName;
                LogUtils.printI(AppUtils.class.getSimpleName(), "getFMPackageName---应用名：" + appName + ", 包名：" + packageName);
                if(packageName.equalsIgnoreCase("com.android.fmradio")){
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static  List<ApplicationInfo> loadAllApp(Context context){

        List<ApplicationInfo> filterList = new ArrayList<>();
        // 获取当前设备上已安装的应用列表
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);

// 遍历应用列表，获取每个应用的图标和名称
        for (ApplicationInfo applicationInfo : applications) {
            String packageName = applicationInfo.packageName;

            if(packageName.equals("org.simalliance.openmobileapi.uicc2terminal")){
                continue;
            }
            if(packageName.equals("com.android.internal.display.cutout.emulation.corner")){
                continue;
            }
            if(packageName.equals("com.android.internal.display.cutout.emulation.double")){
                continue;
            }
            if(packageName.equals("com.android.providers.telephony")){
                continue;
            }
            if(packageName.equals("com.android.providers.calendar")){
                continue;
            }
            if(packageName.equals("com.pve.naviguide")){
                continue;
            }
            if(packageName.equals("com.android.providers.media")){
                continue;
            }
            if(packageName.equals("com.mediatek.location.lppe.main")){
                continue;
            }
            if(packageName.equals("com.android.wallpapercropper")){
                continue;
            }
            if(packageName.equals("com.autochips.quickbootmanager")){
                continue;
            }
            if(packageName.equals("org.simalliance.openmobileapi.service")){
                continue;
            }
            if(packageName.equals("com.android.documentsui")){
                continue;
            }
            if(packageName.equals("android.auto_generated_rro__")){
                continue;
            }
            if(packageName.equals("com.android.externalstorage")){
                continue;
            }
            if(packageName.equals("com.mediatek.ygps")){
                continue;
            }
            if(packageName.equals("com.mediatek.simprocessor")){
                continue;
            }
            if(packageName.equals("com.android.htmlviewer")){
                continue;
            }
            if(packageName.equals("com.android.companiondevicemanager")){
                continue;
            }
            if(packageName.equals("com.android.mms.service")){
                continue;
            }
            if(packageName.equals("com.android.providers.downloads")){
                continue;
            }
            if(packageName.equals("com.mediatek.engineermode")){
                continue;
            }
            if(packageName.equals("com.android.defcontainer")){
                continue;
            }
            if(packageName.equals("com.xy.brakecheck")){
                continue;
            }
            if(packageName.equals("com.android.pacprocessor")){
                continue;
            }
            if(packageName.equals("com.android.simappdialog")){
                continue;
            }
            if(packageName.equals("com.android.internal.display.cutout.emulation.tall")){
                continue;
            }
            if(packageName.equals("com.xyauto.android.autotest")){
                continue;
            }
            if(packageName.equals("com.android.providers.downloads.ui")){
                continue;
            }
            if(packageName.equals("com.xyauto.services")){
                continue;
            }
            if(packageName.equals("com.android.certinstaller")){
                continue;
            }
            if(packageName.equals("com.android.carrierconfig")){
                continue;
            }
            if(packageName.equals("android")){
                continue;
            }
            if(packageName.equals("com.mediatek.gallery.op01")){
                continue;
            }
            if(packageName.equals("com.android.stk")){
                continue;
            }
            if(packageName.equals("com.android.statementservice")){
                continue;
            }
            if(packageName.equals("com.android.settings.intelligence")){
                continue;
            }
            if(packageName.equals("com.android.systemui.theme.dark")){
                continue;
            }
            if(packageName.equals("com.android.providers.settings")){
                continue;
            }
            if(packageName.equals("com.mediatek.miravision.ui")){
                continue;
            }
            if(packageName.equals("com.android.sharedstoragebackup")){
                continue;
            }
            if(packageName.equals("com.pve.installapk")){
                continue;
            }
            if(packageName.equals("org.simalliance.openmobileapi.uicc1terminal")){
                continue;
            }
            if(packageName.equals("com.android.webview")){
                continue;
            }
            if(packageName.equals("com.acloud.stub.logoselector")){
                continue;
            }
            if(packageName.equals("com.android.se")){
                continue;
            }
            if(packageName.equals("com.example")){
                continue;
            }
            if(packageName.equals("com.android.cellbroadcastreceiver")){
                continue;
            }
            if(packageName.equals("android.ext.shared")){
                continue;
            }
            if(packageName.equals("com.mediatek.nlpservice")){
                continue;
            }
            if(packageName.equals("com.mediatek")){
                continue;
            }
            if(packageName.equals("com.android.server.telecom")){
                continue;
            }
            if(packageName.equals("com.android.keychain")){
                continue;
            }
            if(packageName.equals("android.ext.services")){
                continue;
            }
            if(packageName.equals("com.android.calllogbackup")){
                continue;
            }
            if(packageName.equals("com.android.packageinstaller")){
                continue;
            }
            if(packageName.equals("com.android.carrierdefaultapp")){
                continue;
            }
            if(packageName.equals("com.android.proxyhandler")){
                continue;
            }
            if(packageName.equals("com.example.tmedia")){
                continue;
            }
            if(packageName.equals("com.mediatek.callrecorder")){
                continue;
            }
            if(packageName.equals("com.android.smspush")){
                continue;
            }
            if(packageName.equals("com.mediatek.lbs.em2.ui")){
                continue;
            }
            if(packageName.equals("com.acloud.xy.search")){
                continue;
            }
            if(packageName.equals("com.android.vpndialogs")){
                continue;
            }
            if(packageName.equals("com.bumblebee.remindeasy")){
                continue;
            }
            if(packageName.equals("com.android.shell")){
                continue;
            }
            if(packageName.equals("com.android.wallpaperbackup")){
                continue;
            }
            if(packageName.equals("com.android.providers.blockednumber")){
                continue;
            }
            if(packageName.equals("com.android.providers.userdictionary")){
                continue;
            }
            if(packageName.equals("com.android.emergency")){
                continue;
            }
            if(packageName.equals("com.android.location.fused")){
                continue;
            }
            if(packageName.equals("com.mediatek.location.mtknlp")){
                continue;
            }
            if(packageName.equals("com.pve.apsbridge")){
                continue;
            }
            if(packageName.equals("com.xy.lockscreen")){
                continue;
            }
            if(packageName.equals("com.android.wallpaperpicker")){
                continue;
            }
            if(packageName.equals("com.android.captiveportallogin")){
                continue;
            }
            if(packageName.equals("com.android.launcher")){
                continue;
            }
            if(packageName.equals("com.android.fmradio")){
                continue;
            }
            if(packageName.equals("com.android.inputmethod.latin")){
                continue;
            }
            if(packageName.equals("com.android.systemui")){
                continue;
            }
            if(packageName.equals("com.android.storagemanager")){
                continue;
            }
            if(packageName.equals("com.android.inputdevices")){
                continue;
            }
            if(packageName.equals("com.android.phone")){
                continue;
            }
//            if(packageName.equals("com.android.settings")){
//                continue;
//            }
            if(packageName.equals("com.xy.zlinkactivationcode")){
                continue;
            }
            if(packageName.equals("com.xygala.pvcanset")){
                continue;
            }
            if(packageName.equals("com.pve.appsetting")){
                continue;
            }
            if(packageName.equals("com.android.providers.contacts")){
                continue;
            }
            if(packageName.equals("com.kyhero.car.myhost2")){
                continue;
            }
            if(packageName.equals("com.autonavi.amapauto")){
                continue;
            }
            if(packageName.equals("com.xyauto.car")){
                continue;
            }
            if(packageName.equals("com.acloud.stub.extradio")){
                continue;
            }
//            if(packageName.equals(AppPackageType.CAR_PLAY.getTypeValue())){
//                continue;
//            }
//            if(packageName.equals(AppPackageType.PANORAMA.getTypeValue())){
//                continue;
//            }
//            if(packageName.equals(AppPackageType.CAR_METER.getTypeValue())){
//                continue;
//            }
//            if(packageName.equals(AppPackageType.DSP.getTypeValue())){
//                continue;
//            }


            if(packageName.equals("com.acloud.stub.localradio")){ //收音机
                continue;
            }

            if(packageName.equals("com.pve.xy360view")){ //360全景，（不可用）
                continue;
            }

            if(packageName.equals("com.acloud.stub.manual")){ //说明书
                continue;
            }
//            if(packageName.equals("com.xyauto.Settings")){ //车载设置
//                continue;
//            }
            if(packageName.equals("com.xygala.canbus")){ //原车设置
                continue;
            }
            if(packageName.equals("com.acloud.stub.localmusic")){ //本地音乐
                continue;
            }
            if(packageName.equals("com.autochips.bluetooth")){ //蓝牙
                continue;
            }
            if(packageName.equals("com.google.android.webview")){
                continue;
            }
            if(packageName.equals("com.android.providers.partnerbookmarks")){
                continue;
            }
            if(packageName.equals("com.android.bookmarkprovider")){
                continue;
            }
            if(packageName.equals("com.android.bluetooth")){
                continue;
            }

            // 获取应用的图标
            Drawable appIcon = applicationInfo.loadIcon(packageManager);

            // 获取应用的名称
            String appName = (String) applicationInfo.loadLabel(packageManager);

            // 打印应用的图标和名称
            Log.i("应用信息", "应用名称: " + appName + ", 包名=" + packageName);

            filterList.add(applicationInfo);
        }
        return filterList;
    }

    public static PackageInfo getPackageInfo(Context context,String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ApplicationInfo getApplicationInfo(Context context,String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getApplicationInfo(packageName, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //当本应用位于后台时，则将它切换到最前端
    public static void setTopApp(Context context) {
        if (isRunningForeground(context)) {
            return;
        }
        //获取ActivityManager
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获得当前运行的task(任务)
        List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            //找到本应用的 task，并将它切换到前台
            if (taskInfo.topActivity.getPackageName().equals(context.getPackageName())) {
                activityManager.moveTaskToFront(taskInfo.id, 0);
                break;
            }
        }
    }

    //判断本应用是否已经位于最前端：已经位于最前端时，返回 true；否则返回 false
    public static boolean isRunningForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfoList = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfoList) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                return true;
            }
        }
        return false;
    }

    public static String getForegroundPackageName(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager.getRunningTasks(1) == null) {
            return null;
        }
        ActivityManager.RunningTaskInfo mRunningTask =
                activityManager.getRunningTasks(1).get(0);
        if (mRunningTask == null) {
            return null;
        }

        String pkgName = mRunningTask.topActivity.getPackageName();
        return pkgName;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        return height;
    }
}
