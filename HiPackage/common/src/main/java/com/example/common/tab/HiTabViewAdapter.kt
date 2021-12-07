package com.example.common.tab

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.hi_ui.tab.bottom.HiTabBottomInfo

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/21 16:30
 * Description: com.example.common.tab
 */
class HiTabViewAdapter(
    private val fragmentManager: FragmentManager,
    private val mInfoList: List<HiTabBottomInfo>
) {
    var mCurFragment: Fragment? = null

    fun instantiateItem(container: View, position: Int) {
        val mCurTransaction = fragmentManager.beginTransaction()
        mCurFragment?.let {
            mCurTransaction.hide(it)
        }
        val name = "${container.id}:$position"
        var fragment = fragmentManager.findFragmentByTag(name)

        if (fragment != null) {
            mCurTransaction.show(fragment)
        } else {
            fragment = getItem(position)
            if (fragment?.isAdded == false) {
                mCurTransaction.add(container.id, fragment, name)
            }
        }
        mCurFragment = fragment
        mCurTransaction.commitNowAllowingStateLoss()
    }

    private fun getItem(position: Int): Fragment? {
        return mInfoList[position].fragment?.newInstance()
    }

     fun getCount():Int{
        return mInfoList.size
    }

}