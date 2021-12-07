package com.example.hi_ui.tab.bottom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.graphics.toColorInt
import androidx.core.view.children
import com.example.hi_ui.tab.common.IHiTabLayout
import com.example.hi_ui.utils.getDisplayWidthInPx
import com.example.hi_ui.utils.toDp

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/18 23:27
 * Description: com.example.hi_ui.tab.bottom
 */
class HiTabBottomLayout(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs),
    IHiTabLayout<HiTabBottom, HiTabBottomInfo> {
    private val tabSelectedChangeListeners =
        mutableListOf<IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo>>()
    private var selectedInfo: HiTabBottomInfo? = null

    //TabBottom高度
    var tabBottomHeight = 50f

    //TabBottom的头部线条高度
    val bottomLineHeight = 0.5f

    //TabBottom的头部线条颜色
    val bottomLineColor = "#dfe0e1".toColorInt()
    private var infoList: List<HiTabBottomInfo>? = null

    override fun findTab(data: HiTabBottomInfo): HiTabBottom {
        val fl = findViewWithTag<FrameLayout>(TAG_TAB_BOTTOM)
        return fl.children.filter {
            if (it is HiTabBottom)
                it.tabInfo == data
            else false
        }.last() as HiTabBottom
    }

    override fun addTabSelectedChangeListener(listener: IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo>) {
        tabSelectedChangeListeners.add(listener)
    }

    override fun defaultSelected(defaultInfo: HiTabBottomInfo) {
        selected(defaultInfo)
    }

    override fun inflateInfo(infoList: List<HiTabBottomInfo>) {
        if (infoList.isEmpty()) return
        this.infoList = infoList
        // 移除之前已经添加的View
        removeAllViews()
        tabSelectedChangeListeners.clear()
        //Tips：为何不用LinearLayout：当动态改变child大小后Gravity.BOTTOM会失效
        val fl = FrameLayout(context)
        fl.tag = TAG_TAB_BOTTOM
        val width = context.getDisplayWidthInPx() / infoList.size
        infoList.forEachIndexed { index, info ->
            val lp = LayoutParams(width, tabBottomHeight.toDp().toInt())
            lp.gravity = Gravity.BOTTOM
            lp.marginStart = width * index
            fl.addView(HiTabBottom(context, null).apply {
                setHiTabInfo(info)
                addTabSelectedChangeListener(this)
                setOnClickListener {
                    selected(info)
                }
            }, lp)

        }
        selected(infoList[0])
        fl.addView(View(context).apply {
            val lp = LayoutParams(LayoutParams.MATCH_PARENT, bottomLineHeight.toDp().toInt())
            layoutParams = lp
            setBackgroundColor(bottomLineColor)
        })

        val flPrams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        flPrams.gravity = Gravity.BOTTOM
        addView(fl, flPrams)


    }

    private fun selected(info: HiTabBottomInfo) {
        tabSelectedChangeListeners.forEach {
            it.onTabSelectedChange(infoList?.indexOf(info) ?: 0, selectedInfo, info)
        }
        selectedInfo = info
    }

    override fun setBottomColorBackground(color: Int) {
        val fl = findViewWithTag<FrameLayout>(TAG_TAB_BOTTOM)
        fl.setBackgroundColor(color)
    }

    override fun setBottomResIdBackground(resId: Int) {
        val fl = findViewWithTag<FrameLayout>(TAG_TAB_BOTTOM)
        fl.setBackgroundResource(resId)
    }

    companion object {
        const val TAG_TAB_BOTTOM = "TAG_TAB_BOTTOM"
    }
}