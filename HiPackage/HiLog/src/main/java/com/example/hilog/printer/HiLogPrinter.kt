package com.example.hilog.printer

import com.example.hilog.HiLogConfig

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/16 20:56
 * Description: com.example.hilog.log
 * 日志输出接口
 */
interface HiLogPrinter {
    fun print(config: HiLogConfig, level: Int, tag: String, printString: String)
}