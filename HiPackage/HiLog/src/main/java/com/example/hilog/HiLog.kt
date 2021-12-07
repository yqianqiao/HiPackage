package com.example.hilog

import android.util.Log
import com.example.hilog.printer.HiLogPrinter

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/15 21:28
 * Description: com.example.hilog.log
 */
object HiLog {

    fun v(vararg contents: Any) {
        log(type = HiLogType.V, contents = contents)
    }

    fun d(vararg contents: Any) {
        log(type = HiLogType.D, contents = contents)
    }

    fun i(vararg contents: Any) {
        log(type = HiLogType.I, contents = contents)
    }

    fun w(vararg contents: Any) {
        log(type = HiLogType.W, contents = contents)

    }

    fun e(vararg contents: Any) {
        log(type = HiLogType.E, contents = contents)
    }

    fun a(vararg contents: Any) {
        log(type = HiLogType.A, contents = contents)
    }


    fun log(
        config: HiLogConfig = HiLogManager.getInstance().config,
        @HiLogType.TYPE type: Int,
        tag: String = HiLogManager.getInstance().config.getGlobalTag(),
        vararg contents: Any
    ) {

        if (config.enable()) {
            return
        }
        val sb = StringBuilder()
        //是否添加线程信息
        if (config.includeTread()) {
            val threadInfo = HI_THREAD_FORMATTER.format(Thread.currentThread())
            sb.append(threadInfo).append("\n")
        }
        //是否添加堆栈信息
        if (config.stackTraceDepth() > 0) {
            val stackTrace = HI_STACK_TRACE_FORMATTER.format(Throwable().stackTrace)
            sb.append(stackTrace).append("\n")
        }

        val body = parseBody(contents,config)
        sb.append(body)
        val printers = (config.printers())?.toMutableList() ?: HiLogManager.getInstance().printers
        if (printers.isEmpty()) {
            return
        }
        //打印log
        printers.forEach {
            it.print(config, type, tag, sb.toString())
        }
    }

    private fun parseBody(contents: Array<out Any>,config: HiLogConfig): String {
        if (config.injectJsonParser()!=null){
            return config.injectJsonParser()!!.toJson(contents)
        }
        val sb = StringBuilder()
        contents.forEach {
            sb.append(it.toString()).append(";")
        }
        if (sb.isNotEmpty()) {
            sb.deleteAt(sb.length - 1)
        }
        return sb.toString()
    }
}