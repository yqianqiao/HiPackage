package com.example.common.ui.component

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/21 12:50
 * Description: com.example.common.ui.component
 */
abstract class HiBaseFragment : Fragment() {
    protected lateinit var layoutView: View

    @LayoutRes
    abstract fun getLayoutId(): Int
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        layoutView = inflater.inflate(getLayoutId(), container, false)
        return layoutView
    }
}