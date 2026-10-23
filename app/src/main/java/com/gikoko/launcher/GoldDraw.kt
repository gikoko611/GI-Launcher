package com.gikoko.launcher

import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface

object GoldDraw {

    fun draw(c: Canvas, text: String, cx: Float, cy: Float, sizePx: Float, depth: Int, tf: Typeface) {
        val ex = Paint(Paint.ANTI_ALIAS_FLAG)
        ex.textSize = sizePx
        ex.typeface = tf
        ex.textAlign = Paint.Align.CENTER
        ex.color = 0xFF4A3000.toInt()
        val fm = ex.fontMetrics
        val baseY = cy - (fm.ascent + fm.descent) / 2f
        var i = depth
        while (i >= 1) {
            c.drawText(text, cx + i, baseY + i, ex)
            i--
        }
        val w = Math.max(ex.measureText(text), sizePx)
        val face = Paint(ex)
        face.shader = LinearGradient(cx - w, 0f, cx + w, 0f,
            intArrayOf(Ui.GOLD_4, Ui.GOLD_2, 0xFFFFF3C4.toInt(), Ui.GOLD_1, Ui.GOLD_2, Ui.GOLD_4),
            floatArrayOf(0f, 0.2f, 0.4f, 0.5f, 0.6f, 1f),
            Shader.TileMode.CLAMP)
        c.drawText(text, cx, baseY, face)
    }
}
