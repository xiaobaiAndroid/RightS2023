package module.common.utils;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

/**
 * @date： 2023/10/24
 * @author: 78495
*/
public class ShellCommandUtils {

    private static final String TAG = ShellCommandUtils.class.getSimpleName();

    //系统设置
    public static final String PN_SYSTEM_SETTING = " com.xyauto.Settings";

    public  static synchronized void execute(String cmd){
        OutputStream oup = null ;
        try {

            Process process = Runtime.getRuntime().exec(cmd);
            oup =  process.getOutputStream() ;
            oup.flush();
            oup.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (oup != null) {
                oup = null ;
            }
        }
    }

    public static synchronized String executeResult(String cmdStr){

        Process process = null;
        DataOutputStream os = null;
        StringBuffer strb = new StringBuffer();
        try {
            process = Runtime.getRuntime().exec(cmdStr);
            //获取返回值
            InputStream is = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = reader.readLine()) != null) {
                strb.append(line + "\n");
            }
            is.close();
            reader.close();
            process.waitFor();
            process.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (process != null) {
                    process.destroy();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return strb.toString();
    }

    //音量增大
    public static  void volumeIncrease(){
        execute("input keyevent 24");
    }

    //音量减小
    public static void volumeDecrease(){
        execute("input keyevent 25");
    }

    //静音
    public static void silence(){
        execute("input keyevent 164");
    }

    //返回
    public static void back(){
        execute("input keyevent 4");
    }

    //主界面
    public static void home(){
        execute("input keyevent 3");
    }

    //提高屏幕亮度
    public static void brightnessIncrease(){
        execute("input keyevent 221");
    }

    //降低屏幕亮度
    public static void brightnessDecrease(){
        execute("input keyevent 220");
    }

    //系统设置
    public static void systemSetting(){
        execute("input keyevent 176");
    }

    //在焦点处于某文本框时，可以通过 input 命令来输入文本。
    public static void inputText(String content){
        execute("input text "+content);
    }


    //打开app
    public static void openApp(String packageName){
        execute("am start -a "+packageName);
    }

    //获取Android系统版本
    public static String androidVersion(){
       return executeResult("getprop ro.build.version.release");
    }

    //前台activity/进程
    public static String foregroundApp(){
        String executeResult = executeResult("dumpsys activity activities | grep mFocusedActivity");
        if(!TextUtils.isEmpty(executeResult)){
            String activityInfoStart = "* Hist";
            String activityStart = "realActivity=";
            String activityEnd = " baseDir=";
            int start = executeResult.indexOf(activityInfoStart);

            String activitysInfo = executeResult.substring(start + activityInfoStart.length());
            start = activitysInfo.indexOf(activityStart);
            int end = activitysInfo.indexOf(activityEnd);

            String activityName = activitysInfo.substring(start + activityStart.length(),end);
            LogUtils.printI(TAG,"activityName="+activityName);

            String packageStart = "packageName=";
            String packageEnd = "processName=";
             start = activitysInfo.indexOf(packageStart);
             end = activitysInfo.indexOf(packageEnd);

            String packageName = activitysInfo.substring(start + packageStart.length(), end);
            LogUtils.printI(TAG,"packageName="+packageName);
        }
        return executeResult;
    }

//    //修改显示区域, left:左边留白， right：右边留白
//    public static void modifyDisplayArea(int left, int top, int right, int bottom){
//        execute(" wm overscan "+left +","+top+","+right+","+bottom);
//    }
//
//    //恢复原来的显示区域
//    public static void restoreDisplayArea(){
//        execute("wm overscan reset");
//    }
//
//    //修改分辨率
//    public static void modifyDPI(int width, int height){
//        execute("wm size "+width+"x"+height);
//    }
//
//    //恢复分辨率
//    public static void restoreDPI(){
//        execute("wm size reset");
//    }

    //重启系统
    public static void restartSystem(){
        execute("reboot");
    }

    //查看设备显示屏参数, mDisplayId 为 显示屏编号，init 是初始分辨率和屏幕密度，app 的高度比 init 里的要小，表示屏幕底部有虚拟按键
    public static String getDisplayScreenParams(){
        return executeResult("dumpsys window displays");
    }
}
