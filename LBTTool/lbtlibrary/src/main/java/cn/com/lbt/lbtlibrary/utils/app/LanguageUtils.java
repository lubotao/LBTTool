package cn.com.lbt.lbtlibrary.utils.app;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.com.lbt.lbtlibrary.utils.LogPrintUtils;


/**
 * detail: 语言工具类
 * Created by Ttt
 */
public class LanguageUtils {

    private LanguageUtils() {
    }

    // 日志TAG
    private static final String TAG = LanguageUtils.class.getSimpleName();

    /**
     * 获取系统语言
     * @return Locale
     */
    public static String getSystemLanguage() {
        return getSystemPreferredLanguage().getLanguage();
    }

    /**
     * 获取系统首选语言
     * @return Locale
     */
    public static Locale getSystemPreferredLanguage() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    /**
     * 修改系统语言 (App 多语言,单独改变 App 语言)
     * @param context
     * @param locale
     * @return
     */
    public static boolean applyLanguage(Context context, Locale locale) {
        if (context != null && locale != null) {
            try {
                // 获取res资源对象
                Resources resources = context.getResources();
                // 获取设置对象
                Configuration config = resources.getConfiguration();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    // apply locale
                    config.setLocale(locale);
                    context.createConfigurationContext(config);
                } else {
                    // updateConfiguration
                    // 获取屏幕参数：主要是分辨率，像素等。
                    DisplayMetrics dm = resources.getDisplayMetrics();
                    config.locale = locale;
                    // 更新语言
                    resources.updateConfiguration(config, dm);
                }
                return true;
            } catch (Exception e) {
                LogPrintUtils.eTag(TAG, e, "applyLanguage");
            }
        }
        return false;
    }

    /**
     * 修改系统语言 (App 多语言,单独改变 App 语言)
     * @param context
     * @param language
     * @return
     */
    public static boolean applyLanguage(Context context, String language) {
        Locale locale = getSupportLanguage(language);
        if (locale != null) {
            return applyLanguage(context, locale);
        } else { // 如果为null, 则使用系统默认语言
            return applyLanguage(context, getSystemPreferredLanguage());
        }
    }

    // ===

    // 英语
    public static final String ENGLISH = "en";
    // 英语 - 英式
    public static final String UK = "enGB";
    // 英语 - 美式
    public static final String US = "enUS";
    // 法语
    public static final String FRENCH = "fr";
    // 德语
    public static final String GERMAN = "de";
    // 日文
    public static final String JAPAN = "jp";
    // 韩文
    public static final String KOREA = "kr";
    // 中文
    public static final String CHINESE = "zh";
    // 简体中文
    public static final String SIMPLIFIED_CHINESE = "zhCN";
    // 繁体中文 - 默认台湾
    public static final String TRADITIONAL_CHINESE = "zhTW";
    // 台湾
    public static final String TAIWAN_CHINESE = TRADITIONAL_CHINESE;

    private static Map<String, Locale> mSupportLanguages = new HashMap<>(11);

    static {
        // 英语
        mSupportLanguages.put(ENGLISH, Locale.ENGLISH);
        // 英语 - 英式
        mSupportLanguages.put(UK, Locale.UK);
        // 英语 - 美式
        mSupportLanguages.put(US, Locale.US);
        // 法语
        mSupportLanguages.put(FRENCH, Locale.FRENCH);
        // 德语
        mSupportLanguages.put(GERMAN, Locale.GERMAN);
        // 日文
        mSupportLanguages.put(JAPAN, Locale.JAPAN);
        // 韩文
        mSupportLanguages.put(KOREA, Locale.KOREA);
        // 中文
        mSupportLanguages.put(CHINESE, Locale.CHINESE);
        // 简体中文
        mSupportLanguages.put(SIMPLIFIED_CHINESE, Locale.SIMPLIFIED_CHINESE);
        // 繁体中文 - 默认香港
        mSupportLanguages.put(TRADITIONAL_CHINESE, Locale.TRADITIONAL_CHINESE);
        // 台湾
        mSupportLanguages.put(TAIWAN_CHINESE, Locale.TAIWAN);
    }

    /**
     * 获取支持的语言
     * @return
     */
    public static Map<String, Locale> getSupportLanguages() {
        return new HashMap<>(mSupportLanguages);
    }

    /**
     * 添加支持的语言
     * @param language
     * @param locale
     */
    public static void putSupportLanguage(String language, Locale locale) {
        mSupportLanguages.put(language, locale);
    }

    /**
     * 移除支持的语言
     * @param language
     */
    public static void removeSupportLanguage(String language) {
        mSupportLanguages.remove(language);
    }

    /**
     * 是否支持此语言
     * @param language language
     * @return true:支持 false:不支持
     */
    public static boolean isSupportLanguage(String language) {
        return mSupportLanguages.containsKey(language);
    }

    /**
     * 获取支持语言
     * @param language
     * @return 支持返回支持语言，不支持返回系统首选语言
     */
    @TargetApi(Build.VERSION_CODES.N)
    public static Locale getSupportLanguage(String language) {
        if (isSupportLanguage(language)) {
            return mSupportLanguages.get(language);
        }
        return null;
    }
}
