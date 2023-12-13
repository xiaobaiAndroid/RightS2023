package com.android.launcher.util;

import android.os.Environment;
import android.os.StatFs;

/**
 * @date： 2023/10/11
 * @author: 78495
*/
public class StorageUtils {

    public static double getAvailableStorageSize() {
        try {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
                long blockSize = statFs.getBlockSizeLong();
                long availableBlocks = statFs.getAvailableBlocksLong();
                return NumberUtils.roundToTwoDecimalPlaces(bytesToGB(availableBlocks * blockSize));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果外部存储不可用，则返回-1
        return -1.0f;
    }

    public static double bytesToGB(long bytes) {
        return (double) bytes / (1024 * 1024 * 1024);
    }
}
