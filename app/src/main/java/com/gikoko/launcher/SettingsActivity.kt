package com.gikoko.launcher

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class SettingsActivity : Activity() {
    private lateinit var col: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val scroll = ScrollView(this)
        scroll.setFillViewport(true)
        col = LinearLayout(this)
        col.orientation = LinearLayout.VERTICAL
        col.setPadding(
            Ui.dp(this, 24).toInt(),
            Ui.dp(this, 58).toInt(),
            Ui.dp(this, 24).toInt(),
            Ui.dp(this, 40).toInt())
        scroll.addView(col)
        setContentView(scroll)

        val header = GoldTextView(this)
        header.setLabel("G.I KO KO", Ui.dp(this, 38f), 5)
        val hLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        hLp.gravity = Gravity.CENTER_HORIZONTAL
        col.addView(header, hLp)

        val sub = TextView(this)
        sub.text = "LAUNCHER SETTINGS"
        sub.textSize = 13f
        sub.setTextColor(Ui.GOLD_1)
        sub.letterSpacing = 4f
        sub.gravity = Gravity.CENTER
        sub.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        val sLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        sLp.topMargin = Ui.dp(this, 6).toInt()
        col.addView(sub, sLp)

        val powered = TextView(this)
        powered.text = "POWERED BY I KO KO"
        powered.textSize = 11f
        powered.setTextColor(Ui.CYAN)
        powered.letterSpacing = 6f
        powered.gravity = Gravity.CENTER
        powered.setShadowLayer(Ui.dp(this, 6f), 0f, 0f, 0x6635E0FF)
        val pLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        pLp.topMargin = Ui.dp(this, 8).toInt()
        col.addView(powered, pLp)

        val c1 = card("DEFAULT HOME")
        val b1 = Ui.goldButton(this, "SET AS DEFAULT LAUNCHER")
        c1.addView(b1, btnLp())
        b1.setOnClickListener {
            try {
                startActivity(Intent("android.settings.HOME_SETTINGS"))
            } catch (e: Exception) {
                try {
                    startActivity(Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME))
                } catch (e2: Exception) {
                    Toast.makeText(this, "Open system home picker manually", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val c2 = card("WALLPAPER")
        c2.addView(wallBtn("SET HOME WALLPAPER", 1), btnLp())
        c2.addView(wallBtn("SET LOCK SCREEN WALLPAPER", 2), btnLp())
        c2.addView(wallBtn("SET BOTH (HOME + LOCK)", 3), btnLp())

        val c3 = card("CYBER ANIMATION")
        val cb = CheckBox(this)
        cb.text = "ANIMATION ON"
        cb.textSize = 13f
        cb.setTextColor(0xFFE7CF92.toInt())
        cb.isChecked = Ui.pref(this).getBoolean("anim_on", true)
        cb.setOnCheckedChangeListener { _, isChecked ->
            Ui.pref(this).edit().putBoolean("anim_on", isChecked).apply()
        }
        c3.addView(cb)

        val c4 = card("ABOUT")
        val about = TextView(this)
        about.text = "G.I KO KO LAUNCHER v0.0.1\nGold / Myanmar Kanot theme\nCyber 3D animation\nPOWERED BY I KO KO"
        about.textSize = 13f
        about.setTextColor(0xFFD9C48C.toInt())
        c4.addView(about)
    }

    private fun btnLp(): LinearLayout.LayoutParams {
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.topMargin = Ui.dp(this, 10).toInt()
        return lp
    }

    private fun card(title: String): LinearLayout {
        val card = LinearLayout(this)
        card.orientation = LinearLayout.VERTICAL
        val gd = android.graphics.drawable.GradientDrawable()
        gd.setColor(0x14FFE9A3.toInt())
        gd.setStroke(1, 0x59F3C14B.toInt())
        gd.cornerRadius = Ui.dp(this, 18f)
        card.background = gd
        card.setPadding(
            Ui.dp(this, 16).toInt(),
            Ui.dp(this, 14).toInt(),
            Ui.dp(this, 16).toInt(),
            Ui.dp(this, 16).toInt())
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.topMargin = Ui.dp(this, 16).toInt()
        col.addView(card, lp)
        val t = TextView(this)
        t.text = title
        t.textSize = 15f
        t.setTextColor(Ui.GOLD_1)
        t.letterSpacing = 2f
        t.typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD)
        card.addView(t)
        return card
    }

    private fun wallBtn(label: String, flags: Int): TextView {
        val b = Ui.goldButton(this, label)
        b.setOnClickListener {
            Toast.makeText(this, "Generating gold wallpaper...", Toast.LENGTH_SHORT).show()
            Thread {
                try {
                    WallpaperUtil.setWallpaper(this, flags)
                    runOnUiThread {
                        Toast.makeText(this, "Wallpaper set", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this, "Failed: " + e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }.start()
        }
        return b
    }
}
