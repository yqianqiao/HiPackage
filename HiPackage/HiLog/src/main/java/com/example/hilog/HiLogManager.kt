package com.example.hilog

import com.example.hilog.printer.HiLogPrinter

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/15 21:43
 * Description: com.example.hilog.log
 */
class HiLogManager private constructor(
    val config: HiLogConfig,
    var printers: MutableList<HiLogPrinter>
) {
    fun addPrinter(printer: HiLogPrinter){
        printers.add(printer)
    }
    fun removePrinter(printer: HiLogPrinter){
        printers.remove(printer)
    }

    companion object {
       private var manager: HiLogManager? = null
        fun getInstance(): HiLogManager {
            return manager ?: throw error("未初始化 init()")
        }

        fun init(config: HiLogConfig, vararg printers: HiLogPrinter) {
            if (manager == null) {
                synchronized(HiLogManager::class) {
                    if (manager == null) {
                        manager = HiLogManager(config, printers.toMutableList())
                    }
                }
            }
        }
    }

}