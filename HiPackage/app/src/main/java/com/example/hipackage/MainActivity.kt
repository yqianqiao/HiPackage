package com.example.hipackage


import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import com.example.hi_ui.tab.bottom.HiTabBottom
import com.example.hi_ui.tab.bottom.HiTabBottomInfo
import com.example.hi_ui.tab.bottom.HiTabBottomLayout
import com.example.hi_ui.tab.common.IHiTabLayout
import com.example.hi_ui.utils.toDp
import com.example.hilog.HiLog
import com.example.hilog.HiLogConfig
import com.example.hilog.HiLogType


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        findViewById<HiTabBottom>(R.id.tab_bottom).setHiTabInfo(
//            HiTabBottomInfo(
//                "首页",
//                "fonts/iconfont.ttf",
//                getString(R.string.if_home),
//                null,
//                Color.parseColor("#ff656667"),
//                Color.parseColor("#ffd44949")
//            )
//        )
        val layout = findViewById<HiTabBottomLayout>(R.id.hi_layout)

        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire,null)

        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667".toColorInt(),
            "#ffd44949".toColorInt()
        )
        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667".toColorInt(),
            "#ffd44949".toColorInt()
        )
        val infoCategory = HiTabBottomInfo(
            "分类",
            bitmap,
            bitmap
        )
        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667".toColorInt(),
            "#ffd44949".toColorInt()
        )
        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667".toColorInt(),
            "#ffd44949".toColorInt()
        )
        val bottomInfoList = mutableListOf<HiTabBottomInfo>()
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)

        layout.inflateInfo(bottomInfoList)
        layout.defaultSelected(homeInfo)
        layout.setBottomColorBackground("#cccccc".toColorInt())
        layout.addTabSelectedChangeListener(object :
            IHiTabLayout.OnTabSelectedListener<HiTabBottomInfo> {
            override fun onTabSelectedChange(
                index: Int,
                prevInfo: HiTabBottomInfo?,
                nextInfo: HiTabBottomInfo
            ) {
                Log.e("aaaaaaa", "$index" );
            }

        })
        
    }

//    fun but(view: View) {
//
//
//        HiLog.log(object : HiLogConfig() {
//            override fun includeTread(): Boolean {
//                return true
//            }
//
//            override fun stackTraceDepth(): Int {
//                return 2
//            }
//        }, HiLogType.E, "-------", "5566")
//
//    }
}