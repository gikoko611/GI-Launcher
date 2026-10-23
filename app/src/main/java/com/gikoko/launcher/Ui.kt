package com.gikoko.launcher

import android.content.Context
import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.view.Gravity
import android.widget.TextView

object Ui {
    const val GOLD_1 = 0xFFFFE9A3.toInt()
    const val GOLD_2 = 0xFFF3C14B.toInt()
    const val GOLD_3 = 0xFFD4A017.toInt()
    const val GOLD_4 = 0xFF8A5A00.toInt()
    const val CYAN = 0xFF35E0FF.toInt()
    const val INK = 0xFF0A0812.toInt()
    const val INK_2 = 0xFF1B1430.toInt()

    fun dp(ctx: Context, v: Int): Float {
        return v * ctx.resources.displayMetrics.density
    }

    fun dp(ctx: Context, v: Float): Float {
        return v * ctx.resources.displayMetrics.density
    }

    fun pref(ctx: Context): SharedPreferences {
        return ctx.getSharedPreferences("launcher_prefs", Context.MODE_PRIVATE)
    }

    fun goldButton(ctx: Context, text: String): TextView {
        val b = TextView(ctx)
        b.text = text
        b.gravity = Gravity.CENTER
        b.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        b.setTextColor(0xFF0A0812.toInt())
        b.textSize = 13f
        b.letterSpacing = 0.12f
        val base = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM,
            intArrayOf(0xFFFFE9A3.toInt(), 0xFFD4A017.toInt()))
        base.cornerRadius = dp(ctx, 22f)
        b.background = RippleDrawable(ColorStateList.valueOf(0x33FFFFFF), base, null)
        b.setPadding(dp(ctx, 26).toInt(), dp(ctx, 10).toInt(), dp(ctx, 26).toInt(), dp(ctx, 10).toInt())
        return b
    }
}
