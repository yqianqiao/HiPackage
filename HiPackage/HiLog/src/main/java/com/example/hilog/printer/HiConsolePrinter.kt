package com.example.hilog.printer

import android.util.Log
import com.example.hilog.HiLogConfig
import com.example.hilog.MAX_LEN


/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 22:44
 * Description: com.example.hilog.log
 */
class HiConsolePrinter : HiLogPrinter {
    override fun print(config: HiLogConfig, level: Int, tag: String, printString: String) {
        val len: Int = printString.length
        val countOfSub = len / MAX_LEN
        if (countOfSub > 0) {
            val log = StringBuilder()
            var index = 0
            for (i in 0 until countOfSub) {
                log.append(printString.substring(index, index + MAX_LEN))
                index += MAX_LEN
            }
            if (index != len) {
                log.append(printString.substring(index, len))
            }
            Log.println(level, tag, log.toString())
        } else {
            Log.println(level, tag, printString)
        }

    }
}