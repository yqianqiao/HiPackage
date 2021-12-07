package com.example.hi_ui.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.Point
import android.util.TypedValue
import android.view.WindowManager


/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/20 18:21
 * Description: com.example.hi_ui.utils
 */

fun Float.toDp(): Float {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )
}

fun Int.toDp(): Float {
    return this.toFloat().toDp()
}


fun Context.getDisplayWidthInPx(): Int {
    val wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display = wm.defaultDisplay
    val size = Point()
    display.getSize(size)
    return size.x


}