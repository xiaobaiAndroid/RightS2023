package com.android.launcher.util;

import android.os.Environment;
import android.util.Log;

import module.common.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class ZipLogUtil {


    public static void logZip(){
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download";
        String zipPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+"Music"+ File.separator+"left.zip" ;
        File file = new File(zipPath) ;
        if (file.exists()){
            file.delete() ;
        }
        ZipFolder(path,zipPath);
        try {
            upload(APKUtil.UPLOADURL,file);
        } catch (IOException e) {
            e.printStackTrace();
            EventBus.getDefault().post(new MessageEvent(MessageEvent.Type.UPLOAD_FAIL_SERVER_NOT_RESPONSE));
        }
    }


    /**
     * @param url   服务器地址
     * @param file  所要上传的文件
     * @return      响应结果
     * @throws IOException
     */
    public static ResponseBody upload(String url, File file) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file))
                .build();
        Request request = new Request.Builder()
                .header("Authorization", "ClientID" + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response.body();
    }


    /**
     * 压缩文件和文件夹
     *
     * @param srcFileString 要压缩的文件或文件夹
     * @param zipFileString 压缩完成的Zip路径
     * @throws Exception
     */
    public static void ZipFolder(String srcFileString, String zipFileString)   {

        try {
            //创建ZIP
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
            //创建文件
            File file = new File(srcFileString);
            //压缩
            Log.i("zipUtil","---->"+file.getParent()+"==="+file.getAbsolutePath());
            ZipFiles(file.getParent()+ File.separator, file.getName(), outZip);
            //完成和关闭
            outZip.finish();
            outZip.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 压缩文件
     *
     * @param folderString
     * @param fileString
     * @param zipOutputSteam
     * @throws Exception
     *
     */
    private static void ZipFiles(String folderString, String fileString, ZipOutputStream zipOutputSteam) throws Exception {
        Log.i("ZipUtil", "folderString:" + folderString + "\n" +
                "fileString:" + fileString + "\n==========================");
        if (zipOutputSteam == null)
            return;
        File file = new File(folderString + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputSteam.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[1024*10];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputSteam.write(buffer, 0, len);
            }
            zipOutputSteam.closeEntry();
        } else {
            //文件夹
            String fileList[] = file.list();
            //没有子文件和压缩
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputSteam.putNextEntry(zipEntry);
                zipOutputSteam.closeEntry();
            }
            //子文件和递归
            for (int i = 0; i < fileList.length; i++) {
                ZipFiles(folderString + fileString + "/", fileList[i], zipOutputSteam);
            }
        }
    }


}