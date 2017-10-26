package me.haowen.netspeeds.util

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import me.haowen.netspeeds.global.App

object ScreenUtil {

    val screenWidth: Int
        get() {
            val windowManager = App.context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.widthPixels
        }

    val screenHeight: Int
        get() {
            val windowManager = App.context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val dm = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(dm)
            return dm.heightPixels
        }
}