package com.longwei.umier.utils;

import java.util.Date;

public class DateUtils {

    /**
     *
     * @param date current datetime
     * @param period hour
     * @return
     */
    public static Date addHour(Date date, int period) {
        long timestamp = date.getTime() + period * 60 * 60 * 1000;
        return new Date(timestamp);
    }

    /**
     * 获取两个时间相差多少
     * @param curDate
     * @param endDate
     * @return
     */
    public static long diffTime(Date curDate, Date endDate) {
        return endDate.getTime() - curDate.getTime();
    }
}
