package com.example.hilog

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/17 22:01
 * Description: com.example.hilog
 */
object HiStackTraceUtil {
    /**
     *
     */
    fun getCroppedRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        ignorePackage: String?,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        return cropStackTrace(getRealStackTrack(stackTrace, ignorePackage), maxDepth)
    }

    /**
     * 获取除忽略包名之外的堆栈信息
     */
    private fun getRealStackTrack(
        stackTrace: Array<StackTraceElement>,
        ignorePackage: String?
    ): Array<StackTraceElement?> {
        var ignoreDepth = 0
        val allDepth = stackTrace.size
        var className: String
        for (i in allDepth - 1 downTo 0) {
            className = stackTrace[i].className
            if (ignorePackage != null && className.startsWith(ignorePackage)) {
                ignoreDepth = i + 1
                break
            }
        }
        val realDepth = allDepth - ignoreDepth
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(stackTrace, ignoreDepth, realStack, 0, realDepth)
        return realStack
    }

    /**
     * 裁剪堆栈信息
     */
    private fun cropStackTrace(
        callStack: Array<StackTraceElement?>,
        maxDepth: Int
    ): Array<StackTraceElement?> {
        var realDepth: Int = callStack.size
        if (maxDepth > 0) {
            realDepth = maxDepth.coerceAtMost(realDepth)
        }
        val realStack = arrayOfNulls<StackTraceElement>(realDepth)
        System.arraycopy(callStack, 0, realStack, 0, realDepth)
        return realStack
    }
}