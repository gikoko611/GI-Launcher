package com.gikoko.launcher

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

class GoldTextView(context: Context) : View(context) {
    private val extrudePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val facePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    var text = ""
    private var depthPx = 8f
    private var shine = 0f
    private var rotateYAnim: ObjectAnimator? = null

    init {
        val tf = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        extrudePaint.typeface = tf
        extrudePaint.color = 0xFF4A3000.toInt()
        extrudePaint.textAlign = Paint.Align.CENTER
        facePaint.typeface = tf
        facePaint.textAlign = Paint.Align.CENTER
        val va = ValueAnimator.ofFloat(0f, 1f)
        va.duration = 4200
        va.repeatMode = ValueAnimator.REVERSE
        va.repeatCount = ValueAnimator.INFINITE
        va.interpolator = AccelerateDecelerateInterpolator()
        va.addUpdateListener { a ->
            shine = a.animatedValue as Float
            invalidate()
        }
        va.start()
    }

    fun setLabel(t: String, sizePx: Float, extrudeDp: Int) {
        text = t
        extrudePaint.textSize = sizePx
        facePaint.textSize = sizePx
        depthPx = Ui.dp(context, extrudeDp)
        requestLayout()
        invalidate()
    }

    fun startFloat3d() {
        val oa = ObjectAnimator.ofFloat(this, "rotationY", -14f, 14f)
        oa.duration = 3800
        oa.repeatMode = ObjectAnimator.REVERSE
        oa.repeatCount = ObjectAnimator.INFINITE
        oa.interpolator = AccelerateDecelerateInterpolator()
        oa.start()
        rotateYAnim = oa
    }

    override fun onDetachedFromWindow() {
        rotateYAnim?.cancel()
        super.onDetachedFromWindow()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = extrudePaint.measureText(text) + depthPx * 2f + Ui.dp(context, 8f)
        val fm = extrudePaint.fontMetrics
        val h = fm.descent - fm.ascent + depthPx + Ui.dp(context, 4f)
        setMeasuredDimension(resolveSize(w.toInt(), widthMeasureSpec), resolveSize(h.toInt(), heightMeasureSpec))
    }

    override fun onDraw(canvas: Canvas) {
        if (text.isEmpty()) return
        val cx = width / 2f
        val fm = extrudePaint.fontMetrics
        val baseY = (height - (fm.descent - fm.ascent)) / 2f - fm.ascent + depthPx / 2f
        var i = depthPx.toInt()
        while (i >= 1) {
            canvas.drawText(text, cx + i, baseY + i, extrudePaint)
            i--
        }
        val tw = Math.max(extrudePaint.measureText(text), extrudePaint.textSize)
        val g = LinearGradient(cx - tw, 0f, cx + tw, 0f,
            intArrayOf(Ui.GOLD_4, Ui.GOLD_2, 0xFFFFF3C4.toInt(), Ui.GOLD_1, Ui.GOLD_2, Ui.GOLD_4),
            floatArrayOf(0f, 0.2f, 0.4f, 0.5f, 0.6f, 1f),
            Shader.TileMode.CLAMP)
        val m = Matrix()
        m.setTranslate((shine - 0.5f) * tw * 2f, 0f)
        g.setLocalMatrix(m)
        facePaint.shader = g
        canvas.drawText(text, cx, baseY, facePaint)
    }
}
