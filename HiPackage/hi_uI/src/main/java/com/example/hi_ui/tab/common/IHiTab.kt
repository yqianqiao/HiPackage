package com.example.hi_ui.tab.common

import androidx.annotation.Px

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/17 22:51
 * Description: com.example.hi_ui.tab.common
 */
interface IHiTab<D> : IHiTabLayout.OnTabSelectedListener<D> {
    fun setHiTabInfo(data: D)

    fun resetHeight(@Px height: Int)
}