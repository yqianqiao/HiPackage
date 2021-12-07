package com.example.hilog.printer

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 21:38
 * Description: com.example.hilog.log
 */
class HiThreadFormatter : HiLogFormatter<Thread> {
    override fun format(data: Thread?): String {
        return "Thread: ${data?.name}"
    }
}