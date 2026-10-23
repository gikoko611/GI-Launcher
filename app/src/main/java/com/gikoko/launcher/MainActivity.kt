package com.gikoko.launcher

import android.app.Activity
import android.content.Intent
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {
    private val handler = Handler(Looper.getMainLooper())
    private var clockView: GoldTextView? = null
    private var dateView: TextView? = null
    private val timeFmt = SimpleDateFormat("HH:mm", Locale.ENGLISH)
    private val dateFmt = SimpleDateFormat("EEEE • d MMM", Locale.ENGLISH)
    private val tick = object : Runnable {
        override fun run() {
            updateTime()
            handler.postDelayed(this, 30000L)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = FrameLayout(this)
        root.addView(CyberBgView(this), FrameLayout.LayoutParams(-1, -1))
        root.addView(KanotBorderView(this), FrameLayout.LayoutParams(-1, -1))

        val col = LinearLayout(this)
        col.orientation = LinearLayout.VERTICAL
        col.setPadding(
            Ui.dp(this, 22).toInt(),
            Ui.dp(this, 60).toInt(),
            Ui.dp(this, 22).toInt(),
            Ui.dp(this, 44).toInt())

        clockView = GoldTextView(this)
        clockView?.setLabel(timeFmt.format(Date()), Ui.dp(this, 40f), 3)
        val clockLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        clockLp.gravity = Gravity.CENTER_HORIZONTAL
        col.addView(clockView, clockLp)

        dateView = tv(dateFmt.format(Date()).uppercase(Locale.ENGLISH), 12f, 0xFFC9B47E.toInt(), 5f)
        col.addView(dateView)

        val logo = GoldTextView(this)
        logo.setLabel("G.I", Ui.dp(this, 96f), 8)
        logo.startFloat3d()
        val logoLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        logoLp.gravity = Gravity.CENTER_HORIZONTAL
        logoLp.topMargin = Ui.dp(this, 8).toInt()
        col.addView(logo, logoLp)

        val powered = tv("POWERED BY I KO KO", 13f, Ui.CYAN, 7f)
        powered.setShadowLayer(Ui.dp(this, 6f), 0f, 0f, 0x6635E0FF)
        col.addView(powered)

        val scroll = ScrollView(this)
        scroll.isVerticalScrollBarEnabled = false
        val gridHolder = LinearLayout(this)
        gridHolder.orientation = LinearLayout.VERTICAL
        scroll.addView(gridHolder)
        val scrollLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
        scrollLp.weight = 1f
        scrollLp.topMargin = Ui.dp(this, 20).toInt()
        col.addView(scroll, scrollLp)

        val dock = LinearLayout(this)
        dock.orientation = LinearLayout.HORIZONTAL
        dock.gravity = Gravity.CENTER
        dock.setPadding(
            Ui.dp(this, 8).toInt(),
            Ui.dp(this, 10).toInt(),
            Ui.dp(this, 8).toInt(),
            Ui.dp(this, 10).toInt())
        val dg = android.graphics.drawable.GradientDrawable()
        dg.setColor(0x1AFFE9A3.toInt())
        dg.setStroke(Ui.dp(this, 1).toInt(), 0x80F3C14B.toInt())
        dg.cornerRadius = Ui.dp(this, 26f)
        dock.background = dg
        val dockLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        dockLp.topMargin = Ui.dp(this, 12).toInt()
        col.addView(dock, dockLp)
        dockApp(Intent(Intent.ACTION_DIAL, Uri.parse("tel:")), dock)
        dockApp(Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:")), dock)
        dockApp(Intent("android.media.action.STILL_IMAGE_CAMERA"), dock)
        dockApp(Intent(Intent.ACTION_VIEW, Uri.parse("http://github.com")), dock)

        val settingsBtn = Ui.goldButton(this, "SETTINGS")
        val sLp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        sLp.gravity = Gravity.CENTER_HORIZONTAL
        sLp.topMargin = Ui.dp(this, 14).toInt()
        col.addView(settingsBtn, sLp)
        settingsBtn.setOnClickListener { startActivity(Intent(this, SettingsActivity::class.java)) }

        root.addView(col, FrameLayout.LayoutParams(-1, -1))
        setContentView(root)
        loadApps(gridHolder)
        handler.post(tick)
    }

    private fun tv(text: String, sizeSp: Float, color: Int, ls: Float): TextView {
        val t = TextView(this)
        t.text = text
        t.textSize = sizeSp
        t.setTextColor(color)
        t.letterSpacing = ls
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.gravity = Gravity.CENTER_HORIZONTAL
        t.layoutParams = lp
        return t
    }

    private fun updateTime() {
        val now = Date()
        clockView?.setLabel(timeFmt.format(now), Ui.dp(this, 40f), 3)
        dateView?.text = dateFmt.format(now).uppercase(Locale.ENGLISH)
    }

    private fun loadApps(holder: LinearLayout) {
        val pm = packageManager
        val main = Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
        val apps = pm.queryIntentActivities(main, 0)
            .filter { it.activityInfo.packageName != packageName }
            .sortedBy { it.loadLabel(pm).toString().lowercase(Locale.getDefault()) }
        var row: LinearLayout? = null
        for ((i, ri) in apps.withIndex()) {
            if (i % 4 == 0) {
                row = LinearLayout(this)
                row.orientation = LinearLayout.HORIZONTAL
                val rp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                rp.bottomMargin = Ui.dp(this, 12).toInt()
                holder.addView(row, rp)
            }
            row?.addView(appCell(ri), LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        }
    }

    private fun appCell(ri: ResolveInfo): View {
        val cell = LinearLayout(this)
        cell.orientation = LinearLayout.VERTICAL
        cell.gravity = Gravity.CENTER
        val iv = IconFrameView(this)
        val size = Ui.dp(this, 62).toInt()
        cell.addView(iv, LinearLayout.LayoutParams(size, size))
        iv.setIcon(ri.loadIcon(packageManager))
        val label = TextView(this)
        label.text = ri.loadLabel(packageManager).toString()
        label.textSize = 10f
        label.setTextColor(0xFFE7CF92.toInt())
        label.setShadowLayer(Ui.dp(this, 2f), 0f, 0f, 0xFF000000.toInt())
        label.maxLines = 1
        label.ellipsize = TextUtils.TruncateAt.END
        val llp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        llp.topMargin = Ui.dp(this, 6).toInt()
        cell.addView(label, llp)
        cell.setOnClickListener {
            try {
                val launch = packageManager.getLaunchIntentForPackage(ri.activityInfo.packageName)
                if (launch != null) {
                    startActivity(launch)
                } else {
                    startActivity(Intent().setClassName(ri.activityInfo.packageName, ri.activityInfo.name))
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Cannot launch app", Toast.LENGTH_SHORT).show()
            }
        }
        return cell
    }

    private fun dockApp(intent: Intent, dock: LinearLayout) {
        val pm = packageManager
        val list = pm.queryIntentActivities(intent, 0)
        val ri = list.firstOrNull() ?: return
        val cell = appCell(ri)
        val lp = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        lp.marginStart = Ui.dp(this, 8).toInt()
        lp.marginEnd = Ui.dp(this, 8).toInt()
        dock.addView(cell, lp)
    }

    override fun onResume() {
        super.onResume()
        updateTime()
    }

    override fun onDestroy() {
        handler.removeCallbacks(tick)
        super.onDestroy()
    }

    override fun onBackPressed() {
        // launcher stays alive
    }
}
