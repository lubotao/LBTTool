package cn.com.lbt.lbtlibrary.utils.common.assist;

/**
 * detail: 时间均值计算器, 只能用于单线程计时。
 * Created by Ttt
 */
public class TimeAverager {

    /** 计时器 */
    private TimeCounter timeCounter = new TimeCounter();
    /** 均值器 */
    private Averager averager = new Averager();

    /**
     * 一个计时开始
     */
    public long start() {
        return timeCounter.start();
    }

    /**
     * 一个计时结束
     */
    public long end() {
        long time = timeCounter.duration();
        averager.add(time);
        return time;
    }

    /**
     * 一个计时结束,并且启动下次计时。
     */
    public long endAndRestart() {
        long time = timeCounter.durationRestart();
        averager.add(time);
        return time;
    }

    /**
     * 求全部计时均值
     */
    public Number average() {
        return averager.getAverage();
    }

    /**
     * 打印全部时间值
     */
    public void print() {
        averager.print();
    }

    /**
     * 清除数据
     */
    public void clear() {
        averager.clear();
    }
}
