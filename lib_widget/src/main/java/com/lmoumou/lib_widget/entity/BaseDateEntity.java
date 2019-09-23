package com.lmoumou.lib_widget.entity;

/**
 * Created by Zed on 2017/6/23 16:02.
 * dec:
 */

public class BaseDateEntity {

    /**
     * 年
     */
    public int year;
    /**
     * 月
     */
    public int month;
    /**
     * 日
     */
    public int day;

    public BaseDateEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }
}
