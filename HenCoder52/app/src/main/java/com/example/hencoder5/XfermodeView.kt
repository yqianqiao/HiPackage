package com.example.hencoder5

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import ktToDp

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/3/18 21:44
 * Description: com.example.hencoder5
 */
class XfermodeView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val avatar = getImage(200f.ktToDp.toInt())
    private val rectf = RectF(200f, 200f, 400f, 400f)
    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    }

    override fun onDraw(canvas: Canvas) {

        val count = canvas.saveLayer(200f, 200f, 400f, 400f, paint)
        canvas.drawOval(rectf, paint)
        paint.xfermode = xfermode
        canvas.drawBitmap(avatar, 100f, 100f, paint)
        paint.xfermode = null
        canvas.restoreToCount(count)

    }

    private fun getImage(width: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(resources, R.drawable.ic_avater, options)
        options.inJustDecodeBounds = false
        options.inDensity = options.outWidth
        options.inTargetDensity = width
        return BitmapFactory.decodeResource(resources, R.drawable.ic_avater, options)
    }
}