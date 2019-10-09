package com.lmoumou.lib_calendar.bean;

import android.graphics.Color;
import com.lmoumou.lib_calendar.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class AttrsBean {

    private int[] startDate;//日历的开始年、月
    private int[] endDate;//日历的结束年、月
    private int[] singleDate;//单选是默认选中的日期（年、月、日）
    private int[] disableStartDate;//单选时默认选中的年、月、日disableStar
    private int[] disableEndDate;//单选时默认选中的年、月、日
    private boolean showLastNext = true;//是否显示上个月、下个月
    private boolean showLunar = true;//是否显示农历
    private boolean switchChoose = true;//单选时切换月份，是否选中上次的日期
    private int colorSolarNormal = Color.BLACK;//阳历的日期颜色
    private int colorSolarSelect = Color.WHITE;//阳历日期选中的颜色
    private int colorChoose = Color.WHITE;//选中的日期文字颜色
    private int sizeSolar = 14;//阳历日期文字尺寸
    private int dayBg = R.drawable.blue_circle;//选中的背景
    private int subscript = R.mipmap.ic_already_read;//下标
    private Map<String, String> specifyMap;//指定日期对应的文字map
    private int chooseType = 0;//0->单选,1->多选,2->范围选择
    private ArrayList<String> subscriptArray;//需要展示下标的集合

    public ArrayList<String> getSubscriptArray() {
        return subscriptArray;
    }

    public void setSubscriptArray(ArrayList<String> subscriptArray) {
        this.subscriptArray = subscriptArray;
    }

    public int[] getStartDate() {
        return startDate;
    }

    public void setStartDate(int[] startDate) {
        this.startDate = startDate;
    }

    public int[] getEndDate() {
        return endDate;
    }

    public void setEndDate(int[] endDate) {
        this.endDate = endDate;
    }

    public int[] getSingleDate() {
        return singleDate;
    }

    public void setSingleDate(int[] singleDate) {
        this.singleDate = singleDate;
    }


    public int[] getDisableStartDate() {
        return disableStartDate;
    }

    public void setDisableStartDate(int[] disableStartDate) {
        this.disableStartDate = disableStartDate;
    }

    public int[] getDisableEndDate() {
        return disableEndDate;
    }

    public void setDisableEndDate(int[] disableEndDate) {
        this.disableEndDate = disableEndDate;
    }


    public boolean isShowLastNext() {
        return showLastNext;
    }

    public void setShowLastNext(boolean showLastNext) {
        this.showLastNext = showLastNext;
    }

    public boolean isShowLunar() {
        return showLunar;
    }

    public void setShowLunar(boolean showLunar) {
        this.showLunar = showLunar;
    }

    public boolean isSwitchChoose() {
        return switchChoose;
    }

    public void setSwitchChoose(boolean switchChoose) {
        this.switchChoose = switchChoose;
    }

    public int getColorSolarNormal() {
        return colorSolarNormal;
    }

    public void setColorSolarNormal(int colorSolarNormal) {
        this.colorSolarNormal = colorSolarNormal;
    }

    public int getColorSolarSelect() {
        return colorSolarSelect;
    }

    public void setColorSolarSelect(int colorSolarSelect) {
        this.colorSolarSelect = colorSolarSelect;
    }


    public int getColorChoose() {
        return colorChoose;
    }

    public void setColorChoose(int colorChoose) {
        this.colorChoose = colorChoose;
    }

    public int getSizeSolar() {
        return sizeSolar;
    }

    public void setSizeSolar(int sizeSolar) {
        this.sizeSolar = sizeSolar;
    }


    public int getDayBg() {
        return dayBg;
    }

    public void setDayBg(int dayBg) {
        this.dayBg = dayBg;
    }

    public Map<String, String> getSpecifyMap() {
        return specifyMap;
    }

    public void setSpecifyMap(Map<String, String> specifyMap) {
        this.specifyMap = specifyMap;
    }

    public int getChooseType() {
        return chooseType;
    }

    public int getSubscript() {
        return subscript;
    }

    public void setSubscript(int subscript) {
        this.subscript = subscript;
    }

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }
}
