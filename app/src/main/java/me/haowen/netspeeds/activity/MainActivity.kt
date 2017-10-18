package me.haowen.netspeeds.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import me.haowen.netspeeds.widget.CustomViewManager
import me.haowen.netspeeds.global.PreKey
import me.haowen.netspeeds.service.FloatService
import me.haowen.netspeeds.util.Preference
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.button
import org.jetbrains.anko.checkBox
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.verticalLayout

class MainActivity : AppCompatActivity() {

    private var screenX by Preference(PreKey.SCREEN_X, 0)
    private var screenY by Preference(PreKey.SCREEN_Y, 0)
    private var isFixed by Preference(PreKey.IS_FIXED, false)
    private var isTouchable by Preference(PreKey.IS_TOUCHABLE, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            backgroundColor = Color.GRAY

            button {
                text = "记性位置"

                onClick {
                    val top = IntArray(2)
                    CustomViewManager.getInstance(this@MainActivity)?.ankoView?.
                            getLocationOnScreen(top)
                    screenX = top[0]
                    screenY = top[1]
                }
            }

            checkBox {
                isChecked = isFixed
                text = if (isChecked) {
                    "固定"
                } else {
                    "不固定"
                }
                onCheckedChange { _, isChecked ->
                    isFixed = isChecked
                    CustomViewManager.getInstance(this@MainActivity)?.ankoView?.isEnabled =
                            !isChecked
                    text = if (isChecked) {
                        "固定"
                    } else {
                        "不固定"
                    }
                }
            }

            checkBox {
                isChecked = isTouchable
                text = if (isChecked) {
                    "可触摸"
                } else {
                    "不可触摸"
                }
                onCheckedChange { _, isChecked ->
                    isTouchable = isChecked
                    CustomViewManager.getInstance(this@MainActivity)?.setEnableTouch(isChecked)
                    text = if (isChecked) {
                        "可触摸"
                    } else {
                        "不可触摸"
                    }
                }
            }

            button("通知监听权限"){
                onClick {
                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                }
            }
        }

        startService(Intent(this, FloatService::class.java))
    }
}