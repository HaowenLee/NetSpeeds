package me.haowen.netspeeds.service

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import io.reactivex.Flowable
import me.haowen.netspeeds.global.App
import me.haowen.netspeeds.widget.CustomViewManager
import java.util.concurrent.TimeUnit

/**
 * 通知的监听
 * 有通知的时候隐藏，通知隐藏的时候显示
 */
class NotificationCollectorService : NotificationListenerService() {

    companion object {
        const val HIDE_DURATION = 6L
    }

    private val notificationsTime = ArrayList<Long>()

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        CustomViewManager.getInstance(App.context!!)?.isVisible = false
        notificationsTime.add(System.currentTimeMillis())
        Flowable.timer(HIDE_DURATION, TimeUnit.SECONDS)
                .subscribe({
                    if (notificationsTime.lastIndex > 0 && System.currentTimeMillis() -
                            notificationsTime[notificationsTime.lastIndex] < HIDE_DURATION) {
                        return@subscribe
                    }
                    CustomViewManager.getInstance(App.context!!)?.isVisible = true
                }, { t: Throwable ->
                    t.printStackTrace()
                })
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {
        CustomViewManager.getInstance(App.context!!)?.isVisible = true
    }
}