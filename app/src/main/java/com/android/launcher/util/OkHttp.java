package com.android.launcher.util;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class OkHttp {

    private static com.android.launcher.util.OkHttp OkHttp;
    public OkHttpClient mOkHttpClient;
    private OkHttp(){
        mOkHttpClient=  new OkHttpClient.Builder()
                .connectTimeout(600000, TimeUnit.MILLISECONDS)
                .readTimeout(600000, TimeUnit.MILLISECONDS)
                .writeTimeout(600000, TimeUnit.MILLISECONDS)
                .build();
    }


    public static com.android.launcher.util.OkHttp getInstance() {
        if (OkHttp == null) {
            synchronized (com.android.launcher.util.OkHttp.class) {
                if (OkHttp == null) {
                    return OkHttp = new OkHttp();
                }
            }
        }
        return OkHttp;
    }


    /**
     * get请求
     * @param url
     * @return
     */
    public Request doGet(String url){
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();


        return request ;
    }


    /**
     * post求情
     * @param url
     * @param map
     * @return
     */
    public Request doPost(String url, Map<String, String> map) {

        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
        FormBody formBody = builder.build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        return  request ;
    }


    /**
     * post求情
     * @param url
     * @param json
     * @return
     */
    public Request doPostJsonParam(String url, String json) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = RequestBody.create(JSON, json);

        Request request = new Request.Builder()
                .post(requestBody)
                .url(url)
                .build();

        return  request ;
    }


}
