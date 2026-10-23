package com.gikoko.launcher

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.view.View

object KanotDraw {

    fun goldGradient(x0: Float, y0: Float, x1: Float, y1: Float): LinearGradient {
        return LinearGradient(x0, y0, x1, y1,
            intArrayOf(Ui.GOLD_4, Ui.GOLD_1, Ui.GOLD_3, Ui.GOLD_1, Ui.GOLD_4),
            floatArrayOf(0f, 0.35f, 0.5f, 0.65f, 1f),
            Shader.TileMode.CLAMP)
    }

    private fun addBud(p: Path, u: Float, x: Float, tipY: Float, bulbY: Float) {
        val mid = (tipY + bulbY) / 2f
        p.moveTo(x * u, tipY * u)
        p.cubicTo((x + 2.6f) * u, mid * u, (x + 1.8f) * u, (bulbY - (mid - tipY) * 0.4f) * u, x * u, bulbY * u)
        p.cubicTo((x - 1.8f) * u, (bulbY - (mid - tipY) * 0.4f) * u, (x - 2.6f) * u, mid * u, x * u, tipY * u)
        p.close()
    }

    fun vineTile(d: Float): Path {
        val u = d
        val p = Path()
        p.moveTo(0f, 11f * u)
        p.quadTo(5f * u, 2.5f * u, 10f * u, 11f * u)
        p.quadTo(15f * u, 19.5f * u, 20f * u, 11f * u)
        p.quadTo(25f * u, 2.5f * u, 30f * u, 11f * u)
        p.quadTo(35f * u, 19.5f * u, 40f * u, 11f * u)
        addBud(p, u, 5f, 1f, 9.5f)
        addBud(p, u, 15f, 21f, 12.5f)
        addBud(p, u, 25f, 1f, 9.5f)
        addBud(p, u, 35f, 21f, 12.5f)
        return p
    }

    fun drawVineStrip(c: Canvas, strokeP: Paint, fillP: Paint, d: Float, x0: Float, y0: Float, len: Float, vertical: Boolean) {
        val tile = vineTile(d)
        val tileW = 40f * d
        val n = (len / tileW).toInt() + 1
        c.save()
        c.translate(x0, y0)
        if (vertical) c.rotate(90f)
        var i = 0
        while (i < n) {
            c.drawPath(tile, fillP)
            c.drawPath(tile, strokeP)
            c.translate(tileW, 0f)
            i++
        }
        c.restore()
    }

    fun drawMedal(c: Canvas, cx: Float, cy: Float, r: Float, strokeP: Paint, fillP: Paint, alpha: Int) {
        strokeP.alpha = alpha
        fillP.alpha = alpha
        c.drawCircle(cx, cy, r, strokeP)
        strokeP.pathEffect = DashPathEffect(floatArrayOf(r * 0.12f, r * 0.15f), 0f)
        c.drawCircle(cx, cy, r * 0.84f, strokeP)
        strokeP.pathEffect = null
        val petal = Path()
        petal.moveTo(cx, cy - r * 0.98f)
        petal.cubicTo(cx + r * 0.16f, cy - r * 0.6f, cx + r * 0.13f, cy - r * 0.34f, cx, cy - r * 0.28f)
        petal.cubicTo(cx - r * 0.13f, cy - r * 0.34f, cx - r * 0.16f, cy - r * 0.6f, cx, cy - r * 0.98f)
        petal.close()
        val save = c.save()
        var i = 0
        while (i < 8) {
            c.rotate(45f * i, cx, cy)
            c.drawPath(petal, fillP)
            i++
        }
        c.restoreToCount(save)
        c.drawCircle(cx, cy, r * 0.09f, fillP)
    }
}

class KanotBorderView(context: Context) : View(context) {
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG)
    private val fill = Paint(Paint.ANTI_ALIAS_FLAG)
    private val d = Ui.dp(context, 1f)

    init {
        stroke.style = Paint.Style.STROKE
        stroke.strokeWidth = Ui.dp(context, 1.6f)
        fill.style = Paint.Style.FILL
    }

    override fun onDraw(c: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        stroke.shader = KanotDraw.goldGradient(0f, 0f, w, 0f)
        fill.shader = stroke.shader
        KanotDraw.drawVineStrip(c, stroke, fill, d, 0f, d * 34, w, false)
        KanotDraw.drawVineStrip(c, stroke, fill, d, 0f, h - d * 56, w, false)
        stroke.shader = KanotDraw.goldGradient(0f, 0f, 0f, h)
        fill.shader = stroke.shader
        KanotDraw.drawVineStrip(c, stroke, fill, d, d * 22, 0f, h, true)
        KanotDraw.drawVineStrip(c, stroke, fill, d, w, 0f, h, true)
        val mr = d * 15
        val mx = d * 26
        val my = d * 42
        val mB = d * 30
        KanotDraw.drawMedal(c, mx, my, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, w - mx, my, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, mx, h - mB, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, w - mx, h - mB, mr, stroke, fill, 235)
    }
}
