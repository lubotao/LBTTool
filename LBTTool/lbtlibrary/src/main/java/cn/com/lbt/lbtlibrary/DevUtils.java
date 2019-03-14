package cn.com.lbt.lbtlibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.com.lbt.lbtlibrary.utils.JCLogUtils;
import cn.com.lbt.lbtlibrary.utils.LogPrintUtils;
import cn.com.lbt.lbtlibrary.utils.app.AnalysisRecordUtils;
import cn.com.lbt.lbtlibrary.utils.app.FileRecordUtils;
import cn.com.lbt.lbtlibrary.utils.app.HandlerUtils;
import cn.com.lbt.lbtlibrary.utils.app.KeyBoardUtils;
import cn.com.lbt.lbtlibrary.utils.app.cache.DevCache;
import cn.com.lbt.lbtlibrary.utils.app.logger.DevLoggerUtils;
import cn.com.lbt.lbtlibrary.utils.app.share.SharedUtils;
import cn.com.lbt.lbtlibrary.utils.app.toast.toaster.DevToast;


/**
 * 开发工具类
 * @author 37202
 */
public final class DevUtils {

    /**
     * 禁止构造对象,保证只有一个实例
     */
    private DevUtils() {
    }


    /**
     * 日志 TAG
     */
    private static final String TAG = DevUtils.class.getSimpleName();
    /**
     * 全局 Application 对象
     */
    private static Application sApplication;
    /**
     * 全局 Context - getApplicationContext()
     */
    private static Context sContext;
    /**
     * 获取当前线程,主要判断是否属于主线程
     */
    private static Thread sUiThread;
    /**
     * 全局 Handler,便于子线程快捷操作等
     */
    private static Handler sHandler;
    /**
     * 是否内部debug模式
     */
    private static boolean debug = false;

    /**
     * 默认初始化方法 - 必须调用 - Application.onCreate 中调用
     *
     * @param context
     */
    public static void init(Context context) {
        // 设置全局 Context
        initContext(context);
        // 初始化全局 Application
        initApplication(context);
        // 注册 Activity 生命周期监听
        registerActivityLifecycleCallbacks(sApplication);
        // 保存当前线程信息
        sUiThread = Thread.currentThread();
        // 初始化全局Handler - 主线程
        sHandler = new Handler(Looper.getMainLooper());
        // == 初始化工具类相关 ==
        // 初始化Shared 工具类
        SharedUtils.init(context);
        // 初始化缓存类
        DevCache.get(context);
        // 初始化Handler工具类
        HandlerUtils.init(context);
        // 初始化记录文件配置
        FileRecordUtils.init();
        // 初始化记录工具类
        AnalysisRecordUtils.init(context);
        // 初始化 DevLogger 配置
        DevLoggerUtils.init(context);
        // 初始化 Toast
        DevToast.init(sApplication);
    }

    /**
     * 初始化全局 Context
     *
     * @param context
     */
    private static void initContext(Context context) {
        // 如果为null, 才进行判断处理
        if (DevUtils.sContext == null) {
            // 防止传进来的为null
            if (context == null) {
                return;
            }
            DevUtils.sContext = context.getApplicationContext();
        }
    }

    /**
     * 初始化全局 Application
     *
     * @param context
     */
    private static void initApplication(Context context) {
        // 如果为null, 才进行判断处理
        if (DevUtils.sApplication == null) {
            if (context == null) {
                return;
            }
            Application mApplication = null;
            try {
                mApplication = (Application) context.getApplicationContext();
            } catch (Exception e) {
            }
            // 防止传进来的为null
            if (mApplication == null) {
                return;
            }
            DevUtils.sApplication = mApplication;
        }
    }

    /**
     * 获取全局 Context
     *
     * @return
     */
    public static Context getContext() {
        return DevUtils.sContext;
    }

    /**
     * 获取 Context(判断null,视情况返回全局 Context)
     *
     * @param context
     */
    public static Context getContext(Context context) {
        // 进行判断
        if (context != null) {
            return context;
        }
        return DevUtils.sContext;
    }

    /**
     * 获取全局 Application
     *
     * @return
     */
    public static Application getApplication() {
        if (DevUtils.sApplication != null) {
            return DevUtils.sApplication;
        }
        try {
            Application app = getApplicationByReflect();
            if (app != null) {
                // 初始化操作
                init(app);
            }
            return app;
        } catch (Exception e) {
            LogPrintUtils.eTag(TAG, e, "getApplication");
        }
        return null;
    }



    /**
     * 反射获取 Application
     *
     * @return
     */
    private static Application getApplicationByReflect() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> activityThread = Class.forName("android.app.ActivityThread");
            Object thread = activityThread.getMethod("currentActivityThread").invoke(null);
            Object app = activityThread.getMethod("getApplication").invoke(thread);
            if (app == null) {
                throw new NullPointerException("u should init first");
            }
            return (Application) app;
        } catch (Exception e) {
            LogPrintUtils.eTag(TAG, e, "getApplicationByReflect");
        }
        throw new NullPointerException("u should init first");
    }

    // =

    /**
     * 获取Handler
     *
     * @return
     */
    public static Handler getHandler() {
        if (sHandler == null) {
            // 初始化全局Handler - 主线程
            sHandler = new Handler(Looper.getMainLooper()); //Looper.myLooper();
        }
        return sHandler;
    }

    /**
     * 执行UI 线程任务 =>  Activity 的 runOnUiThread(Runnable)
     *
     * @param action 若当前非UI线程则切换到UI线程执行
     */
    public static void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != sUiThread) {
            sHandler.post(action);
        } else {
            action.run();
        }
    }

    /**
     * 执行UI 线程任务 => 延时执行
     *
     * @param action
     * @param delayMillis
     */
    public static void runOnUiThread(Runnable action, long delayMillis) {
        sHandler.postDelayed(action, delayMillis);
    }

    /**
     * 打开日志
     */
    public static void openLog() {
        // 专门打印 Android 日志信息
        LogPrintUtils.setPrintLog(true);
        // 专门打印 Java 日志信息
        JCLogUtils.setPrintLog(true);
    }

    /**
     * 标记debug模式
     */
    public static void openDebug() {
        DevUtils.debug = true;
    }

    /**
     * 判断是否Debug模式
     *
     * @return
     */
    public static boolean isDebug() {
        return debug;
    }

    // == 工具类版本 ==

    /**
     * 获取工具类版本 - VERSION_NAME
     *
     * @return
     */
    public static String getUtilsVersion() {
        return BuildConfig.VERSION_NAME;
    }

    /**
     * 获取工具类版本 - VERSION_CODE
     *
     * @return
     */
    public static int getUtilsVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    // =======================
    // ==== Activity 监听 ====
    // =======================

    /**
     * ActivityLifecycleCallbacks 实现类, 监听 Activity
     */
    private static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();
    // Activity 过滤判断接口
    private static ActivityLifecycleFilter activityLifecycleFilter;
    /**
     * 权限 Activity class name
     */
    public static final String PERMISSION_ACTIVITY_CLASS_NAME = "dev.utils.app.PermissionUtils$PermissionActivity";

    /**
     * 注册绑定Activity 生命周期事件处理
     *
     * @param application
     */
    private static void registerActivityLifecycleCallbacks(Application application) {
        // 先移除监听
        unregisterActivityLifecycleCallbacks(application);
        // 防止为null
        if (application != null) {
            try {
                // 绑定新的监听
                application.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
            } catch (Exception e) {
                LogPrintUtils.eTag(TAG, e, "registerActivityLifecycleCallbacks");
            }
        }
    }

    /**
     * 解除注册 Activity 生命周期事件处理
     *
     * @param application
     */
    private static void unregisterActivityLifecycleCallbacks(Application application) {
        if (application != null) {
            try {
                // 先移除旧的监听
                application.unregisterActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
            } catch (Exception e) {
                LogPrintUtils.eTag(TAG, e, "unregisterActivityLifecycleCallbacks");
            }
        }
    }

    // == 对外公开方法 ==

    /**
     * 获取 Activity 生命周期 相关信息获取接口类
     *
     * @return
     */
    public static ActivityLifecycleGet getActivityLifecycleGet() {
        return ACTIVITY_LIFECYCLE;
    }

    /**
     * 获取 Activity 生命周期 事件监听接口类
     *
     * @return
     */
    public static ActivityLifecycleNotify getActivityLifecycleNotify() {
        return ACTIVITY_LIFECYCLE;
    }

    /**
     * 获取 Top Activity
     *
     * @return
     */
    public static Activity getTopActivity() {
        return ACTIVITY_LIFECYCLE.getTopActivity();
    }

    /**
     * 设置 Activity 生命周期 过滤判断接口
     *
     * @param activityLifecycleFilter
     */
    public static void setActivityLifecycleFilter(ActivityLifecycleFilter activityLifecycleFilter) {
        DevUtils.activityLifecycleFilter = activityLifecycleFilter;
    }

    // == 接口相关 ==

    /**
     * detail: 对Activity的生命周期事件进行集中处理。  ActivityLifecycleCallbacks 实现方法
     * Created by Ttt
     * http://blog.csdn.net/tongcpp/article/details/40344871
     */
    private static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks, ActivityLifecycleGet, ActivityLifecycleNotify {

        // 保存未销毁的 Activity
        final LinkedList<Activity> mActivityList = new LinkedList<>();
        // App 状态改变事件
        final Map<Object, OnAppStatusChangedListener> mStatusListenerMap = new ConcurrentHashMap<>();
        // Activity 销毁事件
        final Map<Activity, Set<OnActivityDestroyedListener>> mDestroyedListenerMap = new ConcurrentHashMap<>();

        // 前台 Activity 总数
        private int mForegroundCount = 0;
        // Activity Configuration 改变次数
        private int mConfigCount = 0;
        // 是否后台 Activity
        private boolean mIsBackground = false;

        // == ActivityLifecycleCallbacks ==

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            setTopActivity(activity);

            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityCreated(activity, savedInstanceState);
            }
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (!mIsBackground) {
                setTopActivity(activity);
            }
            if (mConfigCount < 0) {
                ++mConfigCount;
            } else {
                ++mForegroundCount;
            }

            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityStarted(activity);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            setTopActivity(activity);
            // Activity 准备可见, 设置为非后台 Activity
            if (mIsBackground) {
                mIsBackground = false;
                postStatus(true);
            }

            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityResumed(activity);
            }
        }

        @Override
        public void onActivityPaused(Activity activity) {
            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityPaused(activity);
            }
        }

        @Override
        public void onActivityStopped(Activity activity) {
            // 检测当前的 Activity 是否因为 Configuration 的改变被销毁了
            if (activity.isChangingConfigurations()) {
                --mConfigCount;
            } else {
                --mForegroundCount;
                if (mForegroundCount <= 0) {
                    mIsBackground = true;
                    postStatus(false);
                }
            }

            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityStopped(activity);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivitySaveInstanceState(activity, outState);
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            mActivityList.remove(activity);
            // 通知 Activity 销毁
            consumeOnActivityDestroyedListener(activity);
            // 修复软键盘内存泄漏 在 Activity.onDestroy() 中使用
            KeyBoardUtils.fixSoftInputLeaks(activity);

            if (DevUtils.absActivityLifecycle != null) {
                DevUtils.absActivityLifecycle.onActivityDestroyed(activity);
            }
        }

        // == 内部处理判断方法 ==

        /**
         * 保存 Activity 栈顶
         *
         * @param activity
         */
        private void setTopActivity(final Activity activity) {
            // 判断是否过滤 Activity
            if (ACTIVITY_LIFECYCLE_FILTER.filter(activity)) {
                return;
            }
            // 判断是否已经包含该 Activity
            if (mActivityList.contains(activity)) {
                if (!mActivityList.getLast().equals(activity)) {
                    mActivityList.remove(activity);
                    mActivityList.addLast(activity);
                }
            } else {
                mActivityList.addLast(activity);
            }
        }

        /**
         * 反射获取栈顶 Activity
         *
         * @return
         */
        private Activity getTopActivityByReflect() {
            try {
                @SuppressLint("PrivateApi")
                Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
                Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
                Field activitiesField = activityThreadClass.getDeclaredField("mActivityList");
                activitiesField.setAccessible(true);
                Map activities = (Map) activitiesField.get(activityThread);
                if (activities == null) {
                    return null;
                }
                for (Object activityRecord : activities.values()) {
                    Class activityRecordClass = activityRecord.getClass();
                    Field pausedField = activityRecordClass.getDeclaredField("paused");
                    pausedField.setAccessible(true);
                    if (!pausedField.getBoolean(activityRecord)) {
                        Field activityField = activityRecordClass.getDeclaredField("activity");
                        activityField.setAccessible(true);
                        return (Activity) activityField.get(activityRecord);
                    }
                }
            } catch (Exception e) {
                LogPrintUtils.eTag(TAG, e, "getTopActivityByReflect");
            }
            return null;
        }

        // == ActivityLifecycleGet 方法 ==

        /**
         * 获取最顶部 (当前或最后一个显示) Activity
         *
         * @return
         */
        @Override
        public Activity getTopActivity() {
            if (!mActivityList.isEmpty()) {
                final Activity topActivity = mActivityList.getLast();
                if (topActivity != null) {
                    return topActivity;
                }
            }
            Activity topActivityByReflect = getTopActivityByReflect();
            if (topActivityByReflect != null) {
                setTopActivity(topActivityByReflect);
            }
            return topActivityByReflect;
        }

        /**
         * 判断某个 Activity 是否 Top Activity
         *
         * @param activityClassName Activity.class.getCanonicalName()
         * @return
         */
        @Override
        public boolean isTopActivity(final String activityClassName) {
            if (!TextUtils.isEmpty(activityClassName)) {
                Activity activity = getTopActivity();
                // 判断是否类是否一致
                return (activity != null && activity.getClass().getCanonicalName().equals(activityClassName));
            }
            return false;
        }

        /**
         * 判断某个 Class(Activity) 是否 Top Activity
         *
         * @param clazz Activity.class or this.getClass()
         * @return
         */
        @Override
        public boolean isTopActivity(final Class clazz) {
            if (clazz != null) {
                Activity activity = getTopActivity();
                // 判断是否类是否一致
                return (activity != null && activity.getClass().getCanonicalName().equals(clazz.getCanonicalName()));
            }
            return false;
        }

        /**
         * 判断应用是否后台(不可见)
         *
         * @return
         */
        @Override
        public boolean isBackground() {
            return mIsBackground;
        }

        /**
         * 获取 Activity 总数
         *
         * @return
         */
        @Override
        public int getActivityCount() {
            return mActivityList.size();
        }

        // == ActivityLifecycleNotify ==

        /**
         * 添加 App 状态改变事件监听
         *
         * @param object
         * @param listener
         */
        @Override
        public void addOnAppStatusChangedListener(final Object object, final OnAppStatusChangedListener listener) {
            mStatusListenerMap.put(object, listener);
        }

        /**
         * 移除 App 状态改变事件监听
         *
         * @param object
         */
        @Override
        public void removeOnAppStatusChangedListener(final Object object) {
            mStatusListenerMap.remove(object);
        }

        /**
         * 移除全部 App 状态改变事件监听
         */
        @Override
        public void removeAllOnAppStatusChangedListener() {
            mStatusListenerMap.clear();
        }

        // =

        /**
         * 添加 Activity 销毁通知事件
         *
         * @param activity
         * @param listener
         */
        @Override
        public void addOnActivityDestroyedListener(final Activity activity, final OnActivityDestroyedListener listener) {
            if (activity == null || listener == null) {
                return;
            }
            Set<OnActivityDestroyedListener> listeners;
            if (!mDestroyedListenerMap.containsKey(activity)) {
                listeners = new HashSet<>();
                mDestroyedListenerMap.put(activity, listeners);
            } else {
                listeners = mDestroyedListenerMap.get(activity);
                if (listeners.contains(listener)) {
                    return;
                }
            }
            listeners.add(listener);
        }

        /**
         * 移除 Activity 销毁通知事件
         *
         * @param activity
         */
        @Override
        public void removeOnActivityDestroyedListener(final Activity activity) {
            if (activity == null) {
                return;
            }
            mDestroyedListenerMap.remove(activity);
        }

        /**
         * 移除全部 Activity 销毁通知事件
         */
        @Override
        public void removeAllOnActivityDestroyedListener() {
            mDestroyedListenerMap.clear();
        }


        // == 事件通知相关 ==

        /**
         * 发送状态改变通知
         *
         * @param isForeground
         */
        private void postStatus(final boolean isForeground) {
            if (mStatusListenerMap.isEmpty()) {
                return;
            }
            // 保存到新的集合, 防止 ConcurrentModificationException
            List<OnAppStatusChangedListener> lists = new ArrayList<>(mStatusListenerMap.values());
            // 遍历通知
            for (OnAppStatusChangedListener listener : lists) {
                if (listener != null) {
                    if (isForeground) {
                        listener.onForeground();
                    } else {
                        listener.onBackground();
                    }
                }
            }
        }

        /**
         * 通知 Activity 销毁, 并且消费(移除)监听事件
         *
         * @param activity
         */
        private void consumeOnActivityDestroyedListener(final Activity activity) {
            try {
                // 保存到新的集合, 防止 ConcurrentModificationException
                Set<OnActivityDestroyedListener> sets = new HashSet<>(mDestroyedListenerMap.get(activity));
                // 遍历通知
                for (OnActivityDestroyedListener listener : sets) {
                    if (listener != null) {
                        listener.onActivityDestroyed(activity);
                    }
                }
            } catch (Exception e) {
            }
            // 移除已消费的事件
            removeOnActivityDestroyedListener(activity);
        }
    }

    /**
     * detail: Activity 生命周期 相关信息获取接口
     * Created by Ttt
     */
    public interface ActivityLifecycleGet {

        /**
         * 获取最顶部 (当前或最后一个显示) Activity
         *
         * @return
         */
        Activity getTopActivity();

        /**
         * 判断某个 Activity 是否 Top Activity
         *
         * @param activityClassName Activity.class.getCanonicalName()
         * @return
         */
        boolean isTopActivity(String activityClassName);

        /**
         * 判断某个 Class(Activity) 是否 Top Activity
         *
         * @param clazz Activity.class or this.getClass()
         * @return
         */
        boolean isTopActivity(Class clazz);

        /**
         * 判断应用是否后台(不可见)
         *
         * @return
         */
        boolean isBackground();

        /**
         * 获取 Activity 总数
         *
         * @return
         */
        int getActivityCount();
    }

    /**
     * detail: Activity 生命周期 过滤判断接口
     * Created by Ttt
     */
    public interface ActivityLifecycleFilter {

        /**
         * 判断是否过滤该类(不进行添加等操作)
         *
         * @param activity
         * @return true: return
         */
        boolean filter(Activity activity);
    }

    /**
     * detail: Activity 生命周期 通知接口
     * Created by Ttt
     */
    public interface ActivityLifecycleNotify {

        /**
         * 添加 App 状态改变事件监听
         *
         * @param object
         * @param listener
         */
        void addOnAppStatusChangedListener(Object object, OnAppStatusChangedListener listener);

        /**
         * 移除 App 状态改变事件监听
         *
         * @param object
         */
        void removeOnAppStatusChangedListener(Object object);

        /**
         * 移除全部 App 状态改变事件监听
         */
        void removeAllOnAppStatusChangedListener();

        // =

        /**
         * 添加 Activity 销毁通知事件
         *
         * @param activity
         * @param listener
         */
        void addOnActivityDestroyedListener(Activity activity, OnActivityDestroyedListener listener);

        /**
         * 移除 Activity 销毁通知事件
         *
         * @param activity
         */
        void removeOnActivityDestroyedListener(Activity activity);

        /**
         * 移除全部 Activity 销毁通知事件
         */
        void removeAllOnActivityDestroyedListener();
    }

    /**
     * detail: App 状态改变事件
     * Created by Ttt
     */
    public interface OnAppStatusChangedListener {

        /**
         * 切换到前台
         */
        void onForeground();

        /**
         * 切换到后台
         */
        void onBackground();
    }

    /**
     * detail: Activity 销毁事件
     * Created by Ttt
     */
    public interface OnActivityDestroyedListener {

        /**
         * Activity 销毁通知
         *
         * @param activity
         */
        void onActivityDestroyed(Activity activity);
    }

    // == 接口实现 ==

    private static ActivityLifecycleFilter ACTIVITY_LIFECYCLE_FILTER = new ActivityLifecycleFilter() {
        @Override
        public boolean filter(Activity activity) {
            if (activity != null) {
                if (PERMISSION_ACTIVITY_CLASS_NAME.equals(activity.getClass().getName())) {
                    // 如果相同则不处理(该页面为内部权限框架, 申请权限页面)
                    return true;
                } else {
                    if (activityLifecycleFilter != null) {
                        return activityLifecycleFilter.filter(activity);
                    }
                }
            }
            return false;
        }
    };

    // =

    // ActivityLifecycleCallbacks 抽象类
    private static AbsActivityLifecycle absActivityLifecycle;

    /**
     * 设置 ActivityLifecycle 监听回调
     *
     * @param absActivityLifecycle
     */
    public static void setAbsActivityLifecycle(AbsActivityLifecycle absActivityLifecycle) {
        DevUtils.absActivityLifecycle = absActivityLifecycle;
    }

    /**
     * detail:  ActivityLifecycleCallbacks 抽象类
     * Created by Ttt
     */
    public static abstract class AbsActivityLifecycle implements Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityStarted(Activity activity) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }

        @Override
        public void onActivityStopped(Activity activity) {
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
