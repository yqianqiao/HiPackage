package com.example.common.tab

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import java.lang.IllegalArgumentException
import java.text.ParsePosition

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/21 16:51
 * Description: com.example.common.tab
 * 1.将Fragment的操作内聚
 * 2.提供通用的一些API
 */
class HiFragmentTabView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    var currentPosition = -1
    var mAdapter: HiTabViewAdapter? = null
        set(value) {
            if (field != null || value == null)
                return
            field = value
        }

    fun setCurrentItem(position: Int) {
        if (position < 0 || position > mAdapter?.getCount() ?: -1) {
            return
        }
        if (currentPosition != position) {
            currentPosition = position
            mAdapter?.instantiateItem(this, position)
        }
    }

    fun getCurrentFragment():Fragment? {
        if (mAdapter==null){
            throw IllegalArgumentException("please call setAdapter first.")
        }
        return mAdapter?.mCurFragment
    }
}