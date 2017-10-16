package me.haowen.netspeeds

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

/**
 * 通知的监听
 * 有通知的时候隐藏，通知隐藏的时候显示
 */
class NotificationCollectorService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        CustomViewManager.getInstance(App.context!!)?.isVisiable = false
        println("Hide")
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        CustomViewManager.getInstance(App.context!!)?.isVisiable = true
        println("Show")
    }
}