package com.example.hi_ui.tab.bottom

import android.graphics.Bitmap
import android.graphics.Color
import androidx.fragment.app.Fragment


/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/17 22:51
 * Description: com.example.hi_ui.tab.bottom
 */
class HiTabBottomInfo(val name: String) {
    enum class TabType {
        BITMAP, ICON
    }

    var fragment: Class<out Fragment?>? = null
    var defaultBitmap: Bitmap? = null
    var selectedBitmap: Bitmap? = null
    var iconFont: String? = null

    /**
     * Tips：在Java代码中直接设置iconfont字符串无效，需要定义在string.xml
     */
    var defaultIconName: String? = null
    var selectedIconName: String? = null
    var defaultColor: Int =  Color.parseColor("#ff656667")
    var tintColor: Int =  Color.parseColor("#ffd44949")
    var tabType: TabType? = null

    constructor(name: String, defaultBitmap: Bitmap?, selectedBitmap: Bitmap?):this(name) {
        this.defaultBitmap = defaultBitmap
        this.selectedBitmap = selectedBitmap
        tabType = TabType.BITMAP
    }

    constructor(
        name: String,
        iconFont: String?,
        defaultIconName: String?,
        selectedIconName: String?,
        defaultColor: Int,
        tintColor: Int
    ):this(name) {
        this.iconFont = iconFont
        this.defaultIconName = defaultIconName
        this.selectedIconName = selectedIconName
        this.defaultColor = defaultColor
        this.tintColor = tintColor
        tabType = TabType.ICON
    }
}