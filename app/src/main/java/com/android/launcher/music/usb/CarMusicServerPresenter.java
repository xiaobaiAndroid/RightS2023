package com.android.launcher.music.usb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;

import com.android.launcher.base.PresenterBase;
import com.android.launcher.music.CarMusicItem;
import com.blankj.utilcode.util.DeviceUtils;
import com.bzf.module_db.entity.MusicTable;
import com.bzf.module_db.repository.MusicRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import module.common.utils.AppUtils;
import module.common.utils.LogUtils;
import module.common.utils.StringUtils;

/**
 * @description:
 * @createDate: 2023/5/30
 */
public class CarMusicServerPresenter extends PresenterBase<CarMusicServerIView> {

    private static final String TAG = CarMusicServerPresenter.class.getSimpleName();

    public CarMusicServerPresenter(Context context, CarMusicServerIView iView) {
        super(context,iView);
    }


    public void loadData(String path) {
        ObservableOnSubscribe<List<CarMusicItem>> observable = emitter -> {
            try {


                List<CarMusicItem> list;
                if(TextUtils.isEmpty(path)){
                    list = getMusicListFromOTG(mContext);
                }else{
                    list = getAllMusicPathFromDirectory(new File(path));
                }

                if(list!=null && !list.isEmpty()){
                    Collections.sort(list, (o1, o2) -> o1.getTitle().compareTo(o2.getTitle()));
                }

//                String deviceId = AppUtils.getDeviceId(mContext);
//
//                List<MusicTable> musicTables = new ArrayList<>();
//                for (int i=0; i<list.size(); i++){
//                    CarMusicItem carMusicItem = list.get(i);
//                    new MusicTable()
//                }
//
//                MusicRepository.getInstance().saveData();

                try {
                    LogUtils.printI(TAG, "loadData-----list="+list.size());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                emitter.onNext(list);
                emitter.onComplete();
            } catch (Exception e) {
                e.printStackTrace();
                emitter.onError(e);
            }
        };
        @NonNull Disposable disposable = Observable.create(observable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    if(mIView != null){
                        mIView.getDataResult(list);
                    }
                }, throwable -> {
                    if(mIView != null){
                        mIView.getDataFail();
                    }
                });
        compositeDisposable.add(disposable);
    }



    @SuppressLint("InlinedApi")
    public List<CarMusicItem> getMusicListFromOTG(Context context) {
        // 构造查询媒体库的 Uri
        Uri externalUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        // 获取 Cursor 对象，用于查询媒体库中的音频文件
        String[] projections = {
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM_ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
        };

        Cursor cursor = context.getContentResolver().query(externalUri, projections, MediaStore.Audio.Media.IS_MUSIC + "=?", new String[]{"1"}, null);
        // 遍历 Cursor 并获取音乐信息
        List<CarMusicItem> musicList = new ArrayList<>();
        LogUtils.printI(CarMusicServerPresenter.class.getSimpleName(), "getMusicListFromOTG---cursor.size="+cursor.getCount());

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                String singer = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ARTIST));
                String duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                CarMusicItem music = new CarMusicItem();
                music.setTitle(StringUtils.removeNull(title));
                music.setAlbum(StringUtils.removeNull(album));
                music.setPath(StringUtils.removeNull(path));
                music.setSinger(StringUtils.removeNull(singer));
                musicList.add(music);

                LogUtils.printI(CarMusicServerPresenter.class.getSimpleName(), "music="+music);
            } while (cursor.moveToNext());
            cursor.close();
        }
        LogUtils.printI(CarMusicServerPresenter.class.getSimpleName(), "musicList ---size="+musicList.size());
        return musicList;
    }

    public static List<CarMusicItem> getAllMusicPathFromDirectory(File parentDir) {
        LogUtils.printI(TAG, "parentDir="+parentDir);

        List<CarMusicItem> musicList = new ArrayList<>();
        File[] files = parentDir.listFiles();
        if(files==null){
            return musicList;
        }
        for (File file : files) {
            LogUtils.printI(TAG, "file="+file.getAbsolutePath());
            if (file.isDirectory()) {
                // 如果是目录，则递归遍历
                musicList.addAll(getAllMusicPathFromDirectory(file));
            } else {
                // 判断文件是否为音乐类型
                String fileName = file.getName();
                final String typeMp3 = ".mp3";
                final String typeFlac = ".flac";
                final String typeWav = ".wav";
                final String typeAac = ".aac";

                if (fileName.toLowerCase().endsWith(typeMp3) || fileName.toLowerCase().endsWith(typeFlac) || fileName.toLowerCase().endsWith(typeWav) || fileName.toLowerCase().endsWith(typeAac)) {
                    String path = file.getAbsolutePath();
                    if (!TextUtils.isEmpty(path)) {
                        if(fileName.toLowerCase().endsWith(typeMp3) || fileName.toLowerCase().endsWith(typeWav) || fileName.toLowerCase().endsWith(typeAac)){
                            fileName = fileName.substring(0, fileName.length() - typeMp3.length());
                        }else if(fileName.toLowerCase().endsWith(typeFlac)){
                            fileName = fileName.substring(0, fileName.length() - typeFlac.length());
                        }

                        CarMusicItem music = new CarMusicItem();
                        music.setTitle(fileName);
                        music.setPath(path);
                        music.setAlbum("");
                        music.setSinger("");
                        music.setLyric("");
                        musicList.add(music);

                        LogUtils.printI(TAG, "getAllMusicPathFromDirectory----CarMusicItem="+music);
                    }
                }
            }
        }
        return musicList;
    }
}
