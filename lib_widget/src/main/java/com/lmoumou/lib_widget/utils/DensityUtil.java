package com.lmoumou.lib_widget.utils;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * @author Lmoumou
 * @date : 2019/11/21 9:53
 */
public class DensityUtil {

    public static int dip2px(float value){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}
