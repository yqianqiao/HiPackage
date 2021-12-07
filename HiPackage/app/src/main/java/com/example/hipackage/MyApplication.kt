package com.example.hipackage

import android.app.Application
import com.example.hilog.HiLogConfig
import com.example.hilog.HiLogManager
import com.example.hilog.printer.HiConsolePrinter
import com.google.gson.Gson

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 23:21
 * Description: com.example.hipackage
 */
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object : HiLogConfig() {
            override fun getGlobalTag(): String {
                return "yx"
            }

            override fun enable(): Boolean {
                return true
            }

            override fun injectJsonParser(): JsonParser {
                return object : JsonParser {
                    override fun toJson(src: Any): String {
                        return Gson().toJson(src)
                    }
                }
            }
        }, HiConsolePrinter())
    }
}