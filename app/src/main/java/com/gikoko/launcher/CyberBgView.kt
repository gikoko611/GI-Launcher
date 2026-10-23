package com.gikoko.launcher

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.view.View
import android.view.animation.LinearInterpolator

class CyberBgView(context: Context) : View(context) {
    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val glowPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val horizonPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val particlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bgPaint = Paint()
    private var t = 0f
    private var animOn = Ui.pref(context).getBoolean("anim_on", true)
    private val parts = ArrayList<FloatArray>()

    init {
        gridPaint.style = Paint.Style.STROKE
        gridPaint.strokeWidth = Ui.dp(context, 1.2f)
        glowPaint.style = Paint.Style.STROKE
        glowPaint.strokeWidth = Ui.dp(context, 7f)
        horizonPaint.style = Paint.Style.STROKE
        horizonPaint.strokeWidth = Ui.dp(context, 2f)
        particlePaint.color = Ui.GOLD_1
        val rnd = java.util.Random(7)
        for (i in 0 until 8) {
            parts.add(floatArrayOf(rnd.nextFloat(), 0.4f + rnd.nextFloat() * 1.2f, Ui.dp(context, 2 + rnd.nextInt(3))))
        }
        val va = ValueAnimator.ofFloat(0f, 1f)
        va.duration = 2600
        va.repeatCount = ValueAnimator.INFINITE
        va.interpolator = LinearInterpolator()
        va.addUpdateListener { a ->
            t = a.animatedValue as Float
            if (animOn) invalidate()
        }
        va.start()
    }

    fun setAnim(on: Boolean) {
        animOn = on
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        bgPaint.shader = LinearGradient(0f, 0f, 0f, h.toFloat(), Ui.INK_2, Ui.INK, Shader.TileMode.CLAMP)
        horizonPaint.shader = LinearGradient(0f, 0f, w.toFloat(), 0f,
            intArrayOf(0x0035E0FF, 0x6635E0FF, 0x80FFE9A3.toInt(), 0x6635E0FF, 0x0035E0FF),
            floatArrayOf(0f, 0.32f, 0.5f, 0.68f, 1f),
            Shader.TileMode.CLAMP)
    }

    override fun onDraw(c: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        c.drawRect(0f, 0f, w, h, bgPaint)
        val horizon = h * 0.36f
        val cx = w / 2f
        gridPaint.color = 0x46F3C14B.toInt()
        for (i in -12..12) {
            c.drawLine(cx + i * w * 0.03f, horizon, cx + i * w * 0.1f, h, gridPaint)
        }
        val n = 9
        for (j in 0 until n) {
            val p = (j + t) / n
            val y = horizon + (h - horizon) * p * p
            gridPaint.color = ((40 + (110 * (1f - p)).toInt()) shl 24) or 0x00F3C14B
            c.drawLine(0f, y, w, y, gridPaint)
        }
        glowPaint.color = 0x1AF3C14B.toInt()
        c.drawLine(0f, horizon, w, horizon, glowPaint)
        c.drawLine(0f, horizon, w, horizon, horizonPaint)
        particlePaint.alpha = 170
        for (i in parts.indices) {
            val pr = parts[i]
            val py = h * (1f - ((t * pr[1] + pr[0]) % 1f))
            c.drawCircle(pr[0] * w, py, pr[2], particlePaint)
        }
    }
}
