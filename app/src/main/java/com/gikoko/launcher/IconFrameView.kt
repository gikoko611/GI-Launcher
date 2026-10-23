package com.gikoko.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.Drawable
import android.view.View

class IconFrameView(context: Context) : View(context) {
    private var icon: Drawable? = null
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val curl = Paint(Paint.ANTI_ALIAS_FLAG)
    private val d = Ui.dp(context, 1f)
    private val inset = Ui.dp(context, 8f)
    private val radius = Ui.dp(context, 14f)

    init {
        stroke.style = Paint.Style.STROKE
        stroke.strokeWidth = Ui.dp(context, 1.4f)
        curl.style = Paint.Style.STROKE
        curl.strokeWidth = Ui.dp(context, 2f)
        curl.strokeCap = Paint.Cap.ROUND
    }

    fun setIcon(ic: Drawable?) {
        icon = ic
        invalidate()
    }

    override fun onDraw(c: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        stroke.shader = null
        stroke.color = Ui.GOLD_3
        fill.shader = KanotDraw.goldGradient(0f, 0f, w, 0f)
        KanotDraw.drawMedal(c, w / 2f, h / 2f, w / 2f - d, stroke, fill, 130)
        fill.alpha = 255
        val fx = inset
        val fy = inset
        val fw = w - inset * 2f
        val fh = h - inset * 2f
        fill.shader = RadialGradient(fx + fw / 2f, fy, fw,
            0x2EFFE9A3.toInt(), 0x55120E22.toInt(), Shader.TileMode.CLAMP)
        c.drawRoundRect(fx, fy, fx + fw, fy + fh, radius, radius, fill)
        stroke.shader = null
        stroke.color = 0xB3F3C14B.toInt()
        c.drawRoundRect(fx, fy, fx + fw, fy + fh, radius, radius, stroke)
        drawCurls(c, fx, fy, fw, fh)
        val ic = icon ?: return
        val pad = d * 8
        ic.setBounds((fx + pad).toInt(), (fy + pad).toInt(), (fx + fw - pad).toInt(), (fy + fh - pad).toInt())
        ic.draw(c)
    }

    private fun drawCurls(c: Canvas, fx: Float, fy: Float, fw: Float, fh: Float) {
        val cs = d * 6
        val p = Path()
        curl.color = 0xE6FFE9A3.toInt()
        p.moveTo(fx + cs, fy)
        p.quadTo(fx, fy, fx, fy + cs)
        p.moveTo(fx + fw - cs, fy)
        p.quadTo(fx + fw, fy, fx + fw, fy + cs)
        p.moveTo(fx + fw, fy + fh - cs)
        p.quadTo(fx + fw, fy + fh, fx + fw - cs, fy + fh)
        p.moveTo(fx, fy + fh - cs)
        p.quadTo(fx, fy + fh, fx + cs, fy + fh)
        c.drawPath(p, curl)
    }
}
