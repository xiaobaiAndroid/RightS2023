package com.android.launcher.util;

import android.animation.ValueAnimator;


import java.lang.reflect.Field;

import module.common.utils.LogUtils;

/**
* @description:  ValueAnimator时长错乱或者不起作用
 * https://blog.csdn.net/u011387817/article/details/78628956
* @createDate: 2023/7/15
*/
public class ValueAnimatorUtil {

    /**
     * 如果动画被禁用，则重置动画缩放时长
     */
    public static void resetDurationScaleIfDisable() {
        float durationScale = getDurationScale();
        LogUtils.printI(ValueAnimatorUtil.class.getSimpleName(), "resetDurationScaleIfDisable---durationScale="+durationScale);
        if (durationScale == 0){
            resetDurationScale();
        }
    }

    /**
     * 重置动画缩放时长
     */
    public static void resetDurationScale() {
        try {
            getField().setFloat(null, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static float getDurationScale() {
        try {
            return getField().getFloat(null);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static Field getField() throws NoSuchFieldException {
        Field field = ValueAnimator.class.getDeclaredField("sDurationScale");
        field.setAccessible(true);
        return field;
    }
}