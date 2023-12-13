package com.android.launcher.util;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;

import com.android.launcher.MyApp;

import module.common.MessageEvent;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class APKUtil {


    public static  String UPLOADURL = "http://121.40.19.29:9000/car/log/uploadrightLog";
    public static String SERVERURL = "http://121.40.19.29:9000/car/carApkUpdateRecord/findAllCarUserApk" ;
    public static String DOWNLOADURL = "http://121.40.19.29/file/right.apk" ;

    public static String UPDATESTATUS = "http://121.40.19.29:9000/car/carApkUpdateRecord/updateCarApkUpdateRecordRight" ;

    public static String UPDATELOGSTATUS = "http://121.40.19.29:9000/car/carApkUpdateRecord/updateLogStatusRight" ;

    public static ACache aCache = ACache.get(MyApp.getGlobalContext()) ;
//    public static DownloadProgressDialog progressDialog ;

//    public static Handler handler = new Handler(Looper.getMainLooper()){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//
//            int wha = msg.what ;
//            switch (wha){
//                case 2:
//                    progressDialog=  new DownloadProgressDialog(App.currentActivity);
//                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//                    // 设置ProgressDialog 标题
//                    progressDialog.setTitle("下载提示");
//                    // 设置ProgressDialog 提示信息
//                    progressDialog.setMessage("当前下载进度:");
//                    // 设置ProgressDialog 标题图标
//                    //progressDialog.setIcon(R.drawable.a);
//                    // 设置ProgressDialog 进度条进度
//                    // 设置ProgressDialog 的进度条是否不明确
//                    progressDialog.setIndeterminate(false);
//                    // 设置ProgressDialog 是否可以按退回按键取消
//                    progressDialog.setCancelable(true);
//                    progressDialog.show();
//                    progressDialog.setMax(100);
//                    break;
//                case 1:
//                    int proge = (int) msg.obj ;
//                    progressDialog.setProgress(proge);
//                    break;
//                case 3:
//                    // 下载完成
//                    Log.i("APKdownload","------完成 安装------------") ;
//                    progressDialog.dismiss();
//                    Intent intent = new Intent("xy.android.silent.install") ;
//                    intent.putExtra("ins_apk_pkgname","com.android.launcher") ;
//                    intent.putExtra("ins_apk_pathname","/storage/emulated/0/right.apk") ;
//                    intent.putExtra("force_apk_state",1) ;
//                    App.getGlobalContext().sendBroadcast(intent);
//                break;
//            }
//        }
//    } ;

    /**
     * 检查是否进行安装
     */
    public static void checkUpdate(){
        String deviceId = "";
        try {
            deviceId = Settings.System.getString(MyApp.getGlobalContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        final OkHttp okHttp = OkHttp.getInstance();
        String requestUrl = SERVERURL;
        Log.i("url==", "---" + requestUrl);
        try {
            JSONObject json = new JSONObject();
            json.put("carId",deviceId);
            json.put("location","right");

            Request request  = okHttp.doPostJsonParam(requestUrl, String.valueOf(json));

            okHttp.mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    int code = response.code();
                    if (code == 200) {
                        String reponseString =  response.body().string() ;
                        try {
                            JSONObject jsonObject = new JSONObject(reponseString) ;
                            JSONObject dataJson = jsonObject.getJSONObject("data") ;

                            JSONArray jsonArray = dataJson.getJSONArray("content")  ;


                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject temp = (JSONObject) jsonArray.get(i);
                                String carId = temp.getString("carId");
                                String leftApk = temp.getString("rightApk");
                                String updateleftFlg = temp.getString("updaterightFlg") ;
                                String uplog = temp.getString("uplogright");
                                Log.i("APK","返回内容=========22======="+carId+"===="+leftApk+"==="+updateleftFlg+"=log="+uplog) ;
                                String carIdben = aCache.getAsString("CarNum") ;
                                if (updateleftFlg.equals("T")){
                                    updateStatus(carId,leftApk,updateleftFlg);
//                                    Message message1 = new Message() ;
//                                    message1.what= 2 ;
//                                    handler.sendMessage(message1) ;

                                    EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.APK_DOWNLOAD_SHOW));
                                    downloadApkAndInstall();
                                }
                                if (uplog.equals("T")){
                                    updateLogStatus(carId);
                                    ZipLogUtil.logZip();

                                }


//                                if (carIdben!=null){
//
//                                }else{
//
//                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("APK","返回内容=========1======="+e.getMessage()) ;
                }
            }) ;


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    private static void updateLogStatus(String carId) {
        final OkHttp okHttp = OkHttp.getInstance();

         Map map = new HashMap<String,String>() ;
         map.put("carId",carId);
         map.put("uplogright","F");

         Request request =   okHttp.doPost(UPDATELOGSTATUS,map) ;
        okHttp.mOkHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                int code = response.code();
                Log.i("updateAPK",UPDATESTATUS+"-------2--------") ;
                if (code == 200) {
                    Log.i("updateAPK",UPDATESTATUS+"--------3-------") ;
                    String reponseString =  response.body().string() ;
                }
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("APK","返回内容=========1======="+e.getMessage()) ;
            }
        }) ;

    }

    private static void updateStatus(String carId, String leftApk, String updateleftFlg) {
        try {

            final OkHttp okHttp = OkHttp.getInstance();

            JSONObject json = new JSONObject();
            json.put("carId",carId) ;
            json.put("rightApk",leftApk) ;
            json.put("updaterightFlg","F") ;
            json.put("updaterightDate", FuncUtil.getCurrentDate()) ;
            Request request  = okHttp.doPostJsonParam(UPDATESTATUS, String.valueOf(json));

            Log.i("updateAPK",UPDATESTATUS+"--------1-------") ;
            okHttp.mOkHttpClient.newCall(request).enqueue(new Callback() {

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    int code = response.code();
                    Log.i("updateAPK",UPDATESTATUS+"-------2--------") ;
                    if (code == 200) {
                        Log.i("updateAPK",UPDATESTATUS+"--------3-------") ;
                        String reponseString =  response.body().string() ;
                    }
                }

                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.i("APK","返回内容=========1======="+e.getMessage()) ;
                }
            }) ;


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * 下载apk 安装
     */
    public static void downloadApkAndInstall(){


        OkHttp okHttp = OkHttp.getInstance();
        Request request = okHttp.doGet(APKUtil.DOWNLOADURL) ;
//        createNotification();
        okHttp.mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath =  Environment.getExternalStorageDirectory().getPath()+"/right.apk" ;
                Log.i("APK",savePath+"------------------------===");
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(savePath);
                    file.createNewFile() ;
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
//                        Message message = new Message() ;
//                        message.what = 1 ;
//                        message.obj = progress ;
//                        handler.sendMessage(message) ;

                        MessageEvent messageEvent = new MessageEvent(MessageEvent.Type.APK_DOWNLOAD_PROGRESS);
                        messageEvent.data = progress;
                        EventBus.getDefault().post(messageEvent);
                        Log.i("APK","===================下载进度"+progress) ;
                    }
                    fos.flush();
                    fos.close();

                   Thread.sleep(200);

//                   Message messagecop = new Message() ;
//                   messagecop.what = 3 ;
//                   handler.sendMessage(messagecop);

                   EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.APK_INSTALL));

                } catch (Exception e) {

                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }


//    public static void installtest(){
//        Message messagecop = new Message() ;
//        messagecop.what = 3 ;
//        handler.sendMessage(messagecop);
//    }



}
