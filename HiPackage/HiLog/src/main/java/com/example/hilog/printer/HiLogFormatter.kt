package com.example.hilog.printer

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 21:33
 * Description: com.example.hilog.log
 */
interface HiLogFormatter<T> {
    fun format(data:T?):String?
}