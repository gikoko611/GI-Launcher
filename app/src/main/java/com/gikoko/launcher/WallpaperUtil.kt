package com.gikoko.launcher

import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.Typeface
import java.util.Random

object WallpaperUtil {

    fun setWallpaper(ctx: Context, flags: Int) {
        val dm = ctx.resources.displayMetrics
        var w = dm.widthPixels
        var h = dm.heightPixels
        val max = 1080
        if (h > max) {
            w = w * max / h
            h = max
        }
        val bmp = generate(ctx, w, h)
        try {
            val wm = WallpaperManager.getInstance(ctx)
            if (flags and 1 != 0) {
                wm.setBitmap(bmp, null, true, WallpaperManager.FLAG_SYSTEM)
            }
            if (flags and 2 != 0) {
                wm.setBitmap(bmp, null, true, WallpaperManager.FLAG_LOCK)
            }
        } finally {
            bmp.recycle()
        }
    }

    fun generate(ctx: Context, w: Int, h: Int): Bitmap {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmp)
        val d = Ui.dp(ctx, 1f)
        val bg = Paint()
        bg.shader = LinearGradient(0f, 0f, 0f, h.toFloat(), Ui.INK_2, Ui.INK, Shader.TileMode.CLAMP)
        c.drawRect(0f, 0f, w.toFloat(), h.toFloat(), bg)
        val horizon = h * 0.36f
        val cx = w / 2f
        val grid = Paint(Paint.ANTI_ALIAS_FLAG)
        grid.style = Paint.Style.STROKE
        grid.strokeWidth = Math.max(1f, d)
        grid.color = 0x46F3C14B.toInt()
        for (i in -12..12) {
            c.drawLine(cx + i * w * 0.03f, horizon, cx + i * w * 0.1f, h.toFloat(), grid)
        }
        for (j in 0 until 9) {
            val p = (j + 0.35f) / 9f
            val y = horizon + (h - horizon) * p * p
            c.drawLine(0f, y, w.toFloat(), y, grid)
        }
        val hp = Paint(Paint.ANTI_ALIAS_FLAG)
        hp.style = Paint.Style.STROKE
        hp.strokeWidth = Math.max(2f, d * 2f)
        hp.shader = LinearGradient(0f, horizon, w.toFloat(), horizon,
            intArrayOf(0x0035E0FF, 0x6635E0FF, 0x80FFE9A3.toInt(), 0x6635E0FF, 0x0035E0FF),
            floatArrayOf(0f, 0.32f, 0.5f, 0.68f, 1f),
            Shader.TileMode.CLAMP)
        c.drawLine(0f, horizon, w.toFloat(), horizon, hp)
        val pp = Paint(Paint.ANTI_ALIAS_FLAG)
        pp.color = Ui.GOLD_1
        pp.alpha = 170
        val rnd = Random(3)
        for (i in 0 until 10) {
            c.drawCircle(rnd.nextFloat() * w, rnd.nextFloat() * h, d * (2 + rnd.nextInt(3)), pp)
        }
        val stroke = Paint(Paint.ANTI_ALIAS_FLAG)
        stroke.style = Paint.Style.STROKE
        stroke.strokeWidth = d * 1.6f
        val fill = Paint(Paint.ANTI_ALIAS_FLAG)
        stroke.shader = KanotDraw.goldGradient(0f, 0f, w.toFloat(), 0f)
        fill.shader = stroke.shader
        KanotDraw.drawVineStrip(c, stroke, fill, d, 0f, d * 26, w.toFloat(), false)
        KanotDraw.drawVineStrip(c, stroke, fill, d, 0f, h - d * 48, w.toFloat(), false)
        stroke.shader = KanotDraw.goldGradient(0f, 0f, 0f, h.toFloat())
        fill.shader = stroke.shader
        KanotDraw.drawVineStrip(c, stroke, fill, d, d * 22, 0f, h.toFloat(), true)
        KanotDraw.drawVineStrip(c, stroke, fill, d, w.toFloat(), 0f, h.toFloat(), true)
        val mr = d * 15
        val m = d * 26
        KanotDraw.drawMedal(c, m, m, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, w - m, m, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, m, h - m, mr, stroke, fill, 235)
        KanotDraw.drawMedal(c, w - m, h - m, mr, stroke, fill, 235)
        val tf = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        GoldDraw.draw(c, "G.I", w / 2f, h * 0.40f, w * 0.26f, (d * 6).toInt(), tf)
        val pw = Paint(Paint.ANTI_ALIAS_FLAG)
        pw.color = Ui.CYAN
        pw.textAlign = Paint.Align.CENTER
        pw.textSize = Math.max(14f, w * 0.035f)
        pw.letterSpacing = 0.3f
        pw.typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
        pw.setShadowLayer(d * 6f, 0f, 0f, 0x6635E0FF)
        c.drawText("POWERED BY I KO KO", w / 2f, h * 0.40f + w * 0.19f, pw)
        return bmp
    }
}
