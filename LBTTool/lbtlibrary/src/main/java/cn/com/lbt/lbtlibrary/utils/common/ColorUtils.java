package cn.com.lbt.lbtlibrary.utils.common;

import java.util.HashMap;
import java.util.Locale;

import cn.com.lbt.lbtlibrary.utils.JCLogUtils;


/**
 * detail: 颜色工具类 包括常用的色值
 * Created by Ttt
 */
public final class ColorUtils {

    private ColorUtils() {
    }

    // 日志TAG
    private static final String TAG = ColorUtils.class.getSimpleName();

    /**
     * 0-255 十进值转换成十六进制，如255 就是 ff
     * 255 * 0.x = 十进制 -> 十六进制
     * ============
     * 透明度0-100
     * 00、19、33、4C、66、7F、99、B2、CC、E5、FF
     */

    /** 透明 */
    public static final int TRANSPARENT = 0x00000000;
    /** 白色 */
    public static final int WHITE = 0xffffffff;
    /** 白色 - 半透明 */
    public static final int WHITE_TRANSLUCENT = 0x80ffffff;
    /** 黑色 */
    public static final int BLACK = 0xff000000;
    /** 黑色 - 半透明 */
    public static final int BLACK_TRANSLUCENT = 0x80000000;
    /** 红色 */
    public static final int RED = 0xffff0000;
    /** 红色 - 半透明 */
    public static final int RED_TRANSLUCENT = 0x80ff0000;
    /** 绿色 */
    public static final int GREEN = 0xff00ff00;
    /** 绿色 - 半透明 */
    public static final int GREEN_TRANSLUCENT = 0x8000ff00;
    /** 蓝色 */
    public static final int BLUE = 0xff0000ff;
    /** 蓝色 - 半透明 */
    public static final int BLUE_TRANSLUCENT = 0x800000ff;
    /** 灰色 */
    public static final int GRAY = 0xff969696;
    /** 灰色 - 半透明 */
    public static final int GRAY_TRANSLUCENT = 0x80969696;
    /** 天蓝 */
    public static final int SKYBLUE = 0xff87ceeb;
    /** 橙色 */
    public static final int ORANGE = 0xffffa500;
    /** 金色 */
    public static final int GOLD = 0xffffd700;
    /** 粉色 */
    public static final int PINK = 0xffffc0cb;
    /** 紫红色 */
    public static final int FUCHSIA = 0xffff00ff;
    /** 灰白色 */
    public static final int GRAYWHITE = 0xfff2f2f2;
    /** 紫色 */
    public static final int PURPLE = 0xff800080;
    /** 青色 */
    public static final int CYAN = 0xff00ffff;
    /** 黄色 */
    public static final int YELLOW = 0xffffff00;
    /** 巧克力色 */
    public static final int CHOCOLATE = 0xffd2691e;
    /** 番茄色 */
    public static final int TOMATO = 0xffff6347;
    /** 橙红色 */
    public static final int ORANGERED = 0xffff4500;
    /** 银白色 */
    public static final int SILVER = 0xffc0c0c0;
    /** 深灰色 */
    public static final int DKGRAY = 0xFF444444;
    /** 亮灰色 */
    public static final int LTGRAY = 0xFFCCCCCC;
    /** 洋红色 */
    public static final int MAGENTA = 0xFFFF00FF;
    /** 高光 */
    public static final int HIGHLIGHT = 0x33ffffff;
    /** 低光 */
    public static final int LOWLIGHT = 0x33000000;

    // =

    /**
     * 计算百分比值
     * @param value
     * @param max
     * @return
     */
    public static float percent(float value, float max) {
        if (max <= 0) return 0.0f;
        if (value <= 0) return 0.0f;
        if (value >= max) return 1.0f;
        return value / max;
    }

    /**
     * 计算百分比值
     * @param value
     * @param max
     * @return
     */
    public static float percent(int value, int max) {
        if (max <= 0) return 0.0f;
        if (value <= 0) return 0.0f;
        if (value >= max) return 1.0f;
        return (float) value / (float) max;
    }

    /**
     * 返回的 value 介于 max、min之间，若 value 小于min，返回min，若大于max，返回max
     * @param value
     * @param max
     * @param min
     * @return
     */
    public static int clamp(int value, int max, int min) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * 返回的 value 介于 max、min之间，若 value 小于min，返回min，若大于max，返回max
     * @param value
     * @param max
     * @param min
     * @return
     */
    public static float clamp(float value, float max, float min) {
        return value > max ? max : value < min ? min : value;
    }

    // =

    /**
     * 返回一个颜色中的透明度值(返回10进制)
     * @param color
     * @return
     */
    public static int alpha(int color) {
        return color >>> 24;
    }

    /**
     * 返回一个颜色中的透明度百分比值
     * @param color
     * @return
     */
    public static float alphaPercent(int color) {
        return percent(alpha(color), 255);
    }

    // =

    /**
     * 返回一个颜色中红色的色值(返回10进制)
     * @param color
     * @return
     */
    public static int red(int color) {
        return (color >> 16) & 0xFF;
    }

    /**
     * 返回一个颜色中红色的百分比值
     * @param color
     * @return
     */
    public static float redPercent(int color) {
        return percent(red(color), 255);
    }

    // =

    /**
     * 返回一个颜色中绿色的色值(返回10进制)
     * @param color
     * @return
     */
    public static int green(int color) {
        return (color >> 8) & 0xFF;
    }

    /**
     * 返回一个颜色中绿色的百分比值
     * @param color
     * @return
     */
    public static float greenPercent(int color) {
        return percent(green(color), 255);
    }

    // =

    /**
     * 返回一个颜色中蓝色的色值(返回10进制)
     * @param color
     * @return
     */
    public static int blue(int color) {
        return color & 0xFF;
    }

    /**
     * 返回一个颜色中蓝色的百分比值
     * @param color
     * @return
     */
    public static float bluePercent(int color) {
        return percent(blue(color), 255);
    }

    // =

    /**
     * 根据对应的 red、green、blue 生成一个颜色值
     * @param red [0-255]
     * @param green [0-255]
     * @param blue [0-255]
     */
    public static int rgb(int red, int green, int blue) {
        return 0xff000000 | (red << 16) | (green << 8) | blue;
    }

    /**
     * 根据对应的 red、green、blue 生成一个颜色值
     * @param red [0-255]
     * @param green [0-255]
     * @param blue [0-255]
     */
    public static int rgb(float red, float green, float blue) {
        return 0xff000000 |
                ((int) (red   * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) <<  8) |
                (int) (blue  * 255.0f + 0.5f);
    }

    // =

    /**
     * 根据对应的 alpha, red、green、blue 生成一个颜色值 (含透明度)
     * @param alpha [0-255]
     * @param red [0-255]
     * @param green [0-255]
     * @param blue [0-255]
     */
    public static int argb(int alpha, int red, int green, int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    /**
     * 根据对应的 alpha, red、green、blue 生成一个颜色值 (含透明度)
     * @param alpha [0-255]
     * @param red [0-255]
     * @param green [0-255]
     * @param blue [0-255]
     */
    public static int argb(float alpha, float red, float green, float blue) {
        return ((int) (alpha * 255.0f + 0.5f) << 24) |
                ((int) (red   * 255.0f + 0.5f) << 16) |
                ((int) (green * 255.0f + 0.5f) <<  8) |
                (int) (blue  * 255.0f + 0.5f);
    }

    // =

    /**
     * 判断颜色 RGB 是否有效
     * @param color
     * @return
     */
    public static boolean isRGB(int color) {
        int red = red(color);
        int green = green(color);
        int blue = blue(color);
        return (red <= 255 && red >= 0) &&
                (green <= 255 && green >= 0) &&
                (blue <= 255 && blue >= 0);
    }

    /**
     * 判断颜色 ARGB 是否有效
     * @param color
     * @return
     */
    public static boolean isARGB(int color) {
        int alpha = alpha(color);
        int red = red(color);
        int green = green(color);
        int blue = blue(color);
        return (alpha <= 255 && alpha >= 0) &&
                (red <= 255 && red >= 0) &&
                (green <= 255 && green >= 0) &&
                (blue <= 255 && blue >= 0);
    }

    // =

    /**
     * 设置透明度
     * @param color
     * @param alpha [0-255]
     * @return
     */
    public static int setAlpha(int color, int alpha) {
        return (color & 0x00ffffff) | (alpha << 24);
    }

    /**
     * 设置透明度
     * @param color
     * @param alpha [0-255]
     * @return
     */
    public static int setAlpha(int color, float alpha) {
        return (color & 0x00ffffff) | ((int) (alpha * 255.0f + 0.5f) << 24);
    }

    /**
     * 改变颜色值中的红色色值
     * @param color
     * @param red [0-255]
     * @return
     */
    public static int setRed(int color, int red) {
        return (color & 0xff00ffff) | (red << 16);
    }

    /**
     * 改变颜色值中的红色色值
     * @param color
     * @param red [0-255]
     * @return
     */
    public static int setRed(int color, float red) {
        return (color & 0xff00ffff) | ((int) (red * 255.0f + 0.5f) << 16);
    }

    /**
     * 改变颜色值中的绿色色值
     * @param color
     * @param green [0-255]
     * @return
     */
    public static int setGreen(int color, int green) {
        return (color & 0xffff00ff) | (green << 8);
    }

    /**
     * 改变颜色值中的绿色色值
     * @param color
     * @param green [0-255]
     * @return
     */
    public static int setGreen(int color, float green) {
        return (color & 0xffff00ff) | ((int) (green * 255.0f + 0.5f) << 8);
    }

    /**
     * 改变颜色值中的蓝色色值
     * @param color
     * @param blue [0-255]
     * @return
     */
    public static int setBlue(int color, int blue) {
        return (color & 0xffffff00) | blue;
    }

    /**
     * 改变颜色值中的蓝色色值
     * @param color
     * @param blue [0-255]
     * @return
     */
    public static int setBlue(int color, float blue) {
        return (color & 0xffffff00) | (int) (blue * 255.0f + 0.5f);
    }

    // ==

    /**
     * 解析颜色字符串, 返回对应的颜色值
     * @param colorString
     * @return
     */
    private static int priParseColor(String colorString) {
        if (colorString.charAt(0) == '#') {
            // Use a long to avoid rollovers on #ffXXXXXX
            long color = Long.parseLong(colorString.substring(1), 16);
            if (colorString.length() == 7) {
                // Set the alpha value
                color |= 0x00000000ff000000;
            } else if (colorString.length() != 9) {
                throw new IllegalArgumentException("Unknown color");
            }
            return (int)color;
        } else {
            Integer color = sColorNameMap.get(colorString.toLowerCase(Locale.ROOT));
            if (color != null) {
                return color;
            }
        }
        throw new IllegalArgumentException("Unknown color");
    }

    /**
     * 解析颜色字符串, 返回对应的颜色值
     * 支持的格式:
     * #RRGGBB
     * #AARRGGBB
     * 'red', 'blue', 'green', 'black', 'white', 'gray', 'cyan', 'magenta',
     * 'yellow', 'lightgray', 'darkgray'
     */
    public static int parseColor(String colorString) {
        try {
            return priParseColor(colorString);
        } catch (Exception e) {
            JCLogUtils.eTag(TAG, e, "parseColor");
        }
        return -1;
    }

    /**
     * 颜色值 转换 RGB颜色 字符串
     * @param colorInt
     * @return
     */
    public static String int2RgbString(int colorInt) {
        colorInt = colorInt & 0x00ffffff;
        String colorStr = Integer.toHexString(colorInt);
        while (colorStr.length() < 6) {
            colorStr = "0" + colorStr;
        }
        return "#" + colorStr;
    }

    /**
     * 颜色值 转换 ARGB颜色 字符串
     * @param colorInt
     * @return
     */
    public static String int2ArgbString(int colorInt) {
        String colorString = Integer.toHexString(colorInt);
        while (colorString.length() < 6) {
            colorString = "0" + colorString;
        }
        while (colorString.length() < 8) {
            colorString = "f" + colorString;
        }
        return "#" + colorString;
    }

    // =

    /**
     * 获取随机颜色值
     * @return
     */
    public static int getRandomColor() {
        return getRandomColor(true);
    }

    /**
     * 获取随机颜色值
     * @param supportAlpha
     * @return
     */
    public static int getRandomColor(boolean supportAlpha) {
        int high = supportAlpha ? (int) (Math.random() * 0x100) << 24 : 0xFF000000;
        return high | (int) (Math.random() * 0x1000000);
    }

    /**
     * 判断是否为ARGB格式的十六进制颜色，例如：FF990587
     * @param str
     * @return
     */
    public static boolean judgeColorString(String str) {
        if (str.length() == 8) {
            for (int i = 0; i < str.length(); i++) {
                char cc = str.charAt(i);
                return !(cc != '0' && cc != '1' && cc != '2' && cc != '3' && cc != '4' && cc != '5' && cc != '6' && cc != '7' && cc != '8' && cc != '9' && cc != 'A' && cc != 'B' && cc != 'C' &&
                        cc != 'D' && cc != 'E' && cc != 'F' && cc != 'a' && cc != 'b' && cc != 'c' && cc != 'd' && cc != 'e' && cc != 'f');
            }
        }
        return false;
    }

    // =

    /**
     * 颜色加深(单独修改 RGB值, 不变动透明度)
     * @param colorStr
     * @param darkValue
     * @return
     */
    public static int setDark(String colorStr, int darkValue) {
        int color = parseColor(colorStr);
        if (color == -1) return -1;
        return setDark(color, darkValue);
    }

    /**
     * 颜色加深(单独修改 RGB值, 不变动透明度)
     * @param color
     * @param darkValue
     * @return
     */
    public static int setDark(int color, int darkValue) {
        int red = red(color);
        int green = green(color);
        int blue = blue(color);
        // 进行加深(累减)
        red -= darkValue;
        green -= darkValue;
        blue -= darkValue;
        // 进行设置
        color = setRed(color, clamp(red, 255, 0));
        color = setGreen(color, clamp(green, 255, 0));
        color = setBlue(color, clamp(blue, 255, 0));
        return color;
    }

    /**
     * 颜色变浅, 变亮(单独修改 RGB值, 不变动透明度)
     * @param colorStr
     * @param lightValue
     * @return
     */
    public static int setLight(String colorStr, int lightValue) {
        int color = parseColor(colorStr);
        if (color == -1) return -1;
        return setLight(color, lightValue);
    }

    /**
     * 颜色变浅, 变亮(单独修改 RGB值, 不变动透明度)
     * @param color
     * @param lightValue
     * @return
     */
    public static int setLight(int color, int lightValue) {
        int red = red(color);
        int green = green(color);
        int blue = blue(color);
        // 进行变浅, 变亮(累加)
        red += lightValue;
        green += lightValue;
        blue += lightValue;
        // 进行设置
        color = setRed(color, clamp(red, 255, 0));
        color = setGreen(color, clamp(green, 255, 0));
        color = setBlue(color, clamp(blue, 255, 0));
        return color;
    }

    /**
     * 设置透明度加深
     * @param colorStr
     * @param darkValue
     * @return
     */
    public static int setAlphaDark(String colorStr, int darkValue) {
        int color = parseColor(colorStr);
        if (color == -1) return -1;
        return setAlphaDark(color, darkValue);
    }

    /**
     * 设置透明度加深
     * @param color
     * @param darkValue
     * @return
     */
    public static int setAlphaDark(int color, int darkValue) {
        int alpha = alpha(color);
        // 透明度加深
        alpha += darkValue;
        // 进行设置
        color = setAlpha(color, clamp(alpha, 255, 0));
        return color;
    }

    /**
     * 设置透明度变浅
     * @param colorStr
     * @param lightValue
     * @return
     */
    public static int setAlphaLight(String colorStr, int lightValue) {
        int color = parseColor(colorStr);
        if (color == -1) return -1;
        return setAlphaLight(color, lightValue);
    }

    /**
     * 设置透明度变浅
     * @param color
     * @param lightValue
     * @return
     */
    public static int setAlphaLight(int color, int lightValue) {
        int alpha = alpha(color);
        // 透明度变浅
        alpha -= lightValue;
        // 进行设置
        color = setAlpha(color, clamp(alpha, 255, 0));
        return color;
    }

    // =

    private static final HashMap<String, Integer> sColorNameMap;
    static {
        sColorNameMap = new HashMap<>();
        sColorNameMap.put("transparent", TRANSPARENT);
        sColorNameMap.put("white", WHITE);
        sColorNameMap.put("black", BLACK);
        sColorNameMap.put("red", RED);
        sColorNameMap.put("green", GREEN);
        sColorNameMap.put("blue", BLUE);
        sColorNameMap.put("gray", GRAY);
        sColorNameMap.put("grey", GRAY);
        sColorNameMap.put("skyblue", SKYBLUE);
        sColorNameMap.put("orange", ORANGE);
        sColorNameMap.put("gold", GOLD);
        sColorNameMap.put("pink", PINK);
        sColorNameMap.put("fuchsia", FUCHSIA);
        sColorNameMap.put("graywhite", GRAYWHITE);
        sColorNameMap.put("purple", PURPLE);
        sColorNameMap.put("cyan", CYAN);
        sColorNameMap.put("yellow", YELLOW);
        sColorNameMap.put("chocolate", CHOCOLATE);
        sColorNameMap.put("tomato", TOMATO);
        sColorNameMap.put("orangered", ORANGERED);
        sColorNameMap.put("silver", SILVER);
        sColorNameMap.put("darkgray", DKGRAY);
        sColorNameMap.put("lightgray", LTGRAY);
        sColorNameMap.put("lightgrey", LTGRAY);
        sColorNameMap.put("magenta", MAGENTA);
        sColorNameMap.put("highlight", HIGHLIGHT);
        sColorNameMap.put("lowlight", LOWLIGHT);
        sColorNameMap.put("aqua", 0xFF00FFFF);
        sColorNameMap.put("lime", 0xFF00FF00);
        sColorNameMap.put("maroon", 0xFF800000);
        sColorNameMap.put("navy", 0xFF000080);
        sColorNameMap.put("olive", 0xFF808000);
        sColorNameMap.put("teal", 0xFF008080);
    }
}
