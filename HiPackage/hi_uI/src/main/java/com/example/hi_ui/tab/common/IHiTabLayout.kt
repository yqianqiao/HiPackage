package com.example.hi_ui.tab.common

import android.view.ViewGroup

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/17 22:51
 * Description: com.example.hi_ui.tab.common
 */
interface IHiTabLayout<T : ViewGroup, D> {
    fun findTab(data: D): T
    fun addTabSelectedChangeListener(listener: OnTabSelectedListener<D>)
    fun defaultSelected(defaultInfo: D)
    fun inflateInfo(infoList: List<D>)

    /**
     * 设置底部layout背景
     * @param isRevise 是否设置view属性
     * @param gb 传入颜色或者view
     */

    fun setBottomColorBackground(color: Int)
    fun setBottomResIdBackground(resId: Int)


    interface OnTabSelectedListener<D> {
        fun onTabSelectedChange(index: Int, prevInfo: D?, nextInfo: D)
    }
}