package com.hit.wi.jni;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;

import module.common.utils.LogUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class NKInitDictFile {
    private static final void FileWrite(final InputStream in, final String name) {
        FileOutputStream fos = null;
        try {
            final byte[] buffer = new byte[10240];
            int length = 0;
            fos = new FileOutputStream(
                    FilePath.ROOT_DIR + name);
            while ((length = in.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fos!=null){
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final void FileWriteAppend(InputStream in,
                                              final String name) {
         FileOutputStream fos = null ;
        try {
            final int length = in.available();
            final byte[] buffer = new byte[length];
            in.read(buffer);
           fos = new FileOutputStream(
                    FilePath.ROOT_DIR + name, true);
            fos.write(buffer);
        } catch (final IOException e) {
            e.printStackTrace();
        }finally {
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in !=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final void InitFileOK(final Context context) {
        final SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        final int versionCode =  getVersionCode(context);
        final Editor edit = sp.edit();
        edit.putInt("INIT_FILE_UNDER", versionCode);
        edit.apply();
    }
    public static int getVersionCode(final Context context) {
        final PackageManager packageManager = context.getPackageManager();
        int code = 0;
        try {
            final PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            code = packInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return code;
    }


    private static final boolean IsInitFileOk(final Context context) {
        final SharedPreferences sp = PreferenceManager
                .getDefaultSharedPreferences(context);
        final int nowVersionCode =  getVersionCode(context);
        int isUnder = sp.getInt("INIT_FILE_UNDER", 0);
        return isUnder == nowVersionCode;

    }

    public static final void NKInitWiDict(final Context context) {
//        Log.i("AAAAAAAAAAAA","===========1====="+FilePath.ROOT_DIR);
//        if (IsInitFileOk(context))
//            return;
        final File dictfile = new File(FilePath.ROOT_DIR);
        if (dictfile.exists() != true)
//            Log.i("AAAAAAAAAAAA","==========2======"+FilePath.ROOT_DIR);
            dictfile.mkdir();
        final File emoji = new File(FilePath.ROOT_DIR + "emoji");
        if (emoji.exists() != true)
            emoji.mkdir();
        final File shuangpin = new File(FilePath.ROOT_DIR + "shuangpin");
        if (shuangpin.exists() != true)
            shuangpin.mkdir();
        final File pymap = new File(FilePath.ROOT_DIR + "PinYinDict");
        if (pymap.exists() != true)
            pymap.mkdir();

        final File jjmap = new File(FilePath.ROOT_DIR + "PinYinDict/JiuJianMap");
        if (jjmap.exists() != true)
            jjmap.mkdir();

        final File sdmap = new File(FilePath.ROOT_DIR + "PinYinDict/SegmentDict");
        if (sdmap.exists() != true)
            sdmap.mkdir();

        final AssetManager assetManager = context.getAssets();
        try {
            String filename[] = assetManager.list("dict");
            for (int i = 0; i < filename.length; i++) {
                if (filename[i].equals("PinYinDict")
                        || filename[i].equals("shuangpin")
                        || filename[i].equals("emoji")
                        || filename[i].equals("bigram")
                        || filename[i].equals("word"))
                    continue;
                String path = "dict/" + filename[i];
                InputStream in = assetManager.open(path);
                FileWrite(in, filename[i]);
            }
            filename = null;
            filename = assetManager.list("dict/emoji");
            for (int i = 0; i < filename.length; i++) {
                String path = "dict/emoji/" + filename[i];
                InputStream in = assetManager.open(path);
                FileWrite(in, "emoji/" + filename[i]);
            }
            filename = null;
            filename = assetManager.list("dict/shuangpin");
            for (int i = 0; i < filename.length; i++) {
                String path = "dict/shuangpin/" + filename[i];
                InputStream in = assetManager.open(path);
                FileWrite(in, "shuangpin/" + filename[i]);
            }
            filename = null;
            filename = assetManager.list("dict/bigram");
            for (int i = 0; i < filename.length; i++) {
                final String path = "dict/bigram/" + filename[i];
                final InputStream in = assetManager.open(path);
                if (i == 0) {
                    FileWrite(in, "bigram.dict");
                } else {
                    FileWriteAppend(in, "bigram.dict");
                }
            }
            filename = null;
            filename = assetManager.list("dict/word");
            for (int i = 0; i < filename.length; i++) {
                final String path = "dict/word/" + filename[i];
                final InputStream in = assetManager.open(path);
                if (i == 0) {
                    FileWrite(in, "word.dict");
                } else {
                    FileWriteAppend(in, "word.dict");
                }
            }
            filename = null;
            filename = assetManager.list("dict/PinYinDict/JiuJianMap");
            for (int i = 0; i < filename.length; i++) {
                String path = "dict/PinYinDict/JiuJianMap/" + filename[i];
                InputStream in = assetManager.open(path);
                FileWrite(in, "PinYinDict/JiuJianMap/" + filename[i]);
            }

            filename = null;
            filename = assetManager.list("dict/PinYinDict/SegmentDict");
            for (int i = 0; i < filename.length; i++) {
                String path = "dict/PinYinDict/SegmentDict/" + filename[i];
                InputStream in = assetManager.open(path);
                if (i == 0) {
                    FileWrite(in, "PinYinDict/SegmentDict/segment.bin");
                } else {
                    FileWriteAppend(in, "PinYinDict/SegmentDict/segment.bin");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.printI(NKInitDictFile.class.getSimpleName(), "NKInitWiDict-----e="+e.getMessage());
        }
        InitFileOK(context);
    }


}
