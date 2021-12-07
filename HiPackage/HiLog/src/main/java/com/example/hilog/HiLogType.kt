package com.example.hilog

import android.util.Log
import androidx.annotation.IntDef

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/11/15 21:31
 * Description: com.example.hilog.log
 */
class HiLogType {
    @IntDef(V, D, I, W, E, A)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class TYPE {}

    companion object {
        const val V = Log.VERBOSE
        const val D = Log.DEBUG
        const val I = Log.INFO
        const val W = Log.WARN
        const val E = Log.ERROR
        const val A = Log.ASSERT
    }
}