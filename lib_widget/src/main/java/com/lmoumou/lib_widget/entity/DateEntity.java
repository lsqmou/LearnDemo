package com.lmoumou.lib_widget.entity;

import com.lmoumou.lib_widget.entity.BaseDateEntity;

import java.io.Serializable;

/**
 * @author daij
 * @version 1.0 日历实体类，添加的参数在获取日历实体集合的时候设置
 */
public class DateEntity extends BaseDateEntity implements Serializable {

    private static final long serialVersionUID = -6053739977785155088L;

    /**
     * 日期
     */
    public int date;//20171206
    /**
     * 星期
     */
    public int weekDay;
    /**
     * 是否为当前日期
     */
    public boolean isNowDate;
    /**
     * 是否为本月日期
     */
    public boolean isSelfMonthDate;
    /**
     * 是否有记录
     */
    public boolean hasRecord = false;

    public DateEntity(int year, int month, int day) {
        super(year, month, day);
    }
}
