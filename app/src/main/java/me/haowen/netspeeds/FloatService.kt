package me.haowen.netspeeds

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * author: yyhy
 * date  : 2017/10/11 17:01
 * desc  : 显示悬浮网速
 */
class FloatService : Service() {

    override fun onCreate() {
        super.onCreate()
        CustomViewManager.getInstance(this)?.showFloatViewOnWindow()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}