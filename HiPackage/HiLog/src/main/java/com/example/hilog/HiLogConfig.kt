package com.example.hilog

import com.example.hilog.printer.HiLogPrinter
import com.example.hilog.printer.HiStackTraceFormatter
import com.example.hilog.printer.HiThreadFormatter

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/15 21:41
 * Description: com.example.hilog.log
 */
const val MAX_LEN = 512
val HI_THREAD_FORMATTER = HiThreadFormatter()
val HI_STACK_TRACE_FORMATTER = HiStackTraceFormatter()

abstract class HiLogConfig {

   open fun injectJsonParser(): JsonParser? {
        return null
    }

    /**
     * 默认TAG
     */
    open fun getGlobalTag() = "HiLog"

    open fun enable() = true

    /**
     * 是否包含线程信息
     */
    fun includeTread() = false

    /**
     * 堆栈信息深度
     */
    fun stackTraceDepth() = 5

    /**
     * 用户注册打印器
     */
    fun printers(): Array<HiLogPrinter>? {
        return null
    }


    interface JsonParser {
        fun toJson(src: Any): String
    }

}