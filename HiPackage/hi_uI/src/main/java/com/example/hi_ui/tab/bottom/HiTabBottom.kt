package com.example.hi_ui.tab.bottom

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import com.example.hi_ui.R
import com.example.hi_ui.tab.common.IHiTab


/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/17 22:51
 * Description: com.example.hi_ui.tab.bottom
 */
class HiTabBottom(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs),
    IHiTab<HiTabBottomInfo> {
     lateinit var tabInfo: HiTabBottomInfo
    private var tabImageView: ImageView
    private var tabIconView: TextView
    private var tabNameView: TextView

    init {
        LayoutInflater.from(context).inflate(R.layout.hi_tab_bottom, this)
        tabImageView = findViewById(R.id.iv_image)
        tabIconView = findViewById(R.id.tv_icon)
        tabNameView = findViewById(R.id.tv_name)
    }

    override fun setHiTabInfo(data: HiTabBottomInfo) {
        tabInfo = data
        inflateInfo(selected = false, init = true)
    }

    private fun inflateInfo(selected: Boolean, init: Boolean) {
        if (tabInfo.tabType === HiTabBottomInfo.TabType.ICON) {
            if (init) {
                tabImageView.visibility = GONE
                tabIconView.visibility = VISIBLE
                val typeface = Typeface.createFromAsset(context.assets, tabInfo.iconFont)
                tabIconView.typeface = typeface
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.text = tabInfo.name
                }
            }
            if (selected) {
                tabIconView.text =
                    if (TextUtils.isEmpty(tabInfo.selectedIconName)) tabInfo.defaultIconName else tabInfo.selectedIconName
                tabIconView.setTextColor(tabInfo.tintColor)
                tabNameView.setTextColor(tabInfo.tintColor)
            } else {
                tabIconView.text = tabInfo.defaultIconName
                tabIconView.setTextColor(tabInfo.defaultColor)
                tabNameView.setTextColor(tabInfo.defaultColor)
            }
        } else if (tabInfo.tabType === HiTabBottomInfo.TabType.BITMAP) {
            if (init) {
                tabImageView.visibility = VISIBLE
                tabIconView.visibility = GONE
                if (!TextUtils.isEmpty(tabInfo.name)) {
                    tabNameView.text = tabInfo.name
                }
            }
            if (selected) {
                tabImageView.setImageBitmap(tabInfo.selectedBitmap)
            } else {
                tabImageView.setImageBitmap(tabInfo.defaultBitmap)
            }
        }
    }

    override fun resetHeight(height: Int) {
        val layoutParams = layoutParams
        layoutParams.height = height
        setLayoutParams(layoutParams)
        tabImageView.visibility = View.GONE
    }

    override fun onTabSelectedChange(
        index: Int,
        prevInfo: HiTabBottomInfo?,
        nextInfo: HiTabBottomInfo
    ) {
        if (prevInfo != tabInfo && nextInfo != tabInfo || prevInfo == nextInfo){
            return
        }
        if (prevInfo==tabInfo){
            inflateInfo(selected = false, init = false)
        }else{
            inflateInfo(selected = true, init = false)
        }
    }

}