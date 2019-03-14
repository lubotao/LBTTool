package cn.com.lbt.lbtlibrary.utils.common;

import java.io.Closeable;

import cn.com.lbt.lbtlibrary.utils.JCLogUtils;


/**
 * detail: 关闭工具类 - (关闭IO流等)
 * Created by Ttt
 */
public final class CloseUtils {

    private CloseUtils() {
    }

    // 日志TAG
    private static final String TAG = CloseUtils.class.getSimpleName();

    /**
     * 关闭 IO
     * @param closeables closeables
     */
    public static void closeIO(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception e) {
                    JCLogUtils.eTag(TAG, e, "closeIO");
                }
            }
        }
    }

    /**
     * 安静关闭 IO
     * @param closeables closeables
     */
    public static void closeIOQuietly(final Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (Exception ignore) {
                    JCLogUtils.eTag(TAG, ignore, "closeIO");
                }
            }
        }
    }
}
