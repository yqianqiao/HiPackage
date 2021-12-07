package com.example.hilog.printer

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 21:40
 * Description: com.example.hilog.log
 */
class HiStackTraceFormatter : HiLogFormatter<Array<StackTraceElement?>> {
    override fun format(data: Array<StackTraceElement?>?): String? {
        val sb = StringBuilder(128)
        return if (data == null || data.isEmpty()) {
            null
        } else if (data.size == 1) {
            "\t─ " + data[0].toString()
        } else {
            var i = 0
            val len: Int = data.size
            while (i < len) {
                if (i == 0) {
                    sb.append("stackTrace:  \n")
                }
                if (i != len - 1) {
                    sb.append("\t├ ")
                    sb.append(data[i].toString())
                    sb.append("\n")
                } else {
                    sb.append("\t└ ")
                    sb.append(data[i].toString())
                }
                i++
            }
            sb.toString()
        }
    }
}