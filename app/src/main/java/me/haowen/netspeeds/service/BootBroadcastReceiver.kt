package me.haowen.netspeeds.service

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * 开机自启动广播
 */
class BootBroadcastReceiver : BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
        startKeepService(context)
    }

    /**
     * 启动双服务
     */
    private fun startKeepService(context: Context) {
        context.startService(Intent(context, LocalKeepService::class.java))
        context.startService(Intent(context, RemoteKeepService::class.java))
    }
}
