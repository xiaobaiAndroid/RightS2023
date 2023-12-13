package com.android.launcher.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import com.android.launcher.type.LanguageType;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import module.common.utils.LogUtils;

/**
* @description:
* @createDate: 2023/9/11
*/
public class LanguageUtils {

    /**
    * @description: 中文的语言环境
    * @createDate: 2023/9/11
    */
    public static boolean isCN(){
        try {
            String languageCode = getLanguage();
            if("zh".equalsIgnoreCase(languageCode)){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @NotNull
    private static String getLanguage() {
        Locale locale = Locale.getDefault();
        String languageCode = locale.getLanguage(); // 语言代码
        String countryCode = locale.getCountry(); // 国家代码
        String displayName = locale.getDisplayName(); // 语言环境的显示名称
        LogUtils.printI(LanguageUtils.class.getSimpleName(), "languageCode="+languageCode +",countryCode="+countryCode +" ,displayName="+displayName);

        return languageCode;
    }

    /**
    * @description: 设置语言
    * @createDate: 2023/9/13
    */
    public static synchronized void setLang(Context context, LanguageType languageType) {
        try {
            LogUtils.printI(LanguageUtils.class.getSimpleName(), "languageType="+languageType.name());
            String lang = null;
            if(languageType == LanguageType.ZH){
                lang="zh";
            }else if(languageType == LanguageType.EN){
                lang = "en";
            }
            if(lang == null){
                return;
            }

            // 创建一个新的Locale对象，指定需要切换的语言环境
            Locale locale = new Locale(lang);

            // 获取当前的 Resources 对象
            Resources resources = context.getResources();

            // 获取 Configuration 对象
            Configuration config = resources.getConfiguration();

            // 设置语言环境
            config.setLocale(locale);

            // 更新 Configuration 对象
            resources.updateConfiguration(config, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
