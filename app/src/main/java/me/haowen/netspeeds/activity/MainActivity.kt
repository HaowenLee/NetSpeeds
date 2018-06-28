package me.haowen.netspeeds.activity

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import me.haowen.netspeeds.R
import me.haowen.netspeeds.global.PreKey
import me.haowen.netspeeds.service.LocalKeepService
import me.haowen.netspeeds.service.RemoteKeepService
import me.haowen.netspeeds.util.Preference
import me.haowen.netspeeds.widget.CustomViewManager
import me.haowen.netspeeds.widget.SettingStyle
import me.haowen.netspeeds.widget.dialog.HintDialog
import me.haowen.netspeeds.widget.settingItem
import org.jetbrains.anko.*


class MainActivity : AppCompatActivity() {

    private var screenX by Preference(PreKey.SCREEN_X, 0)
    private var screenY by Preference(PreKey.SCREEN_Y, 0)
    private var isFixed by Preference(PreKey.IS_FIXED, false)
    private var isTouchable by Preference(PreKey.IS_TOUCHABLE, true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            backgroundColor = Color.parseColor("#ebebeb")

            relativeLayout {

                if(isL()) {
                    elevation = dip(3).toFloat()
                }

                backgroundColor = Color.parseColor("#f6f6f6")

                textView("设置") {
                    textSize = 20f
                    textColor = Color.parseColor("#616161")
                }.lparams {
                    gravity = Gravity.CENTER
                }

            }.lparams(matchParent, dip(48))

            scrollView {

                verticalLayout {

                    space { }.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_save,
                            R.drawable.ic_save_white,
                            "记忆位置",
                            itemClickListener = {
                                val top = IntArray(2)
                                CustomViewManager.getInstance(this@MainActivity)?.ankoView?.
                                        getLocationOnScreen(top)
                                screenX = top[0]
                                screenY = top[1]
                                toast("位置已存储")
                            })

                    space {}.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_pushpin,
                            R.drawable.ic_pushpin_white,
                            "固定",
                            settingStyle = SettingStyle.TOP,
                            hintIconEnable = true,
                            hintClickListener = {
                                HintDialog.Builder(this@MainActivity)
                                        .content("当您希望悬浮框可以固定在移动的位置的时候，可以打" +
                                                "开次开关。一旦打开此开关，悬浮框将不可拖动。我们建议您将悬浮框" +
                                                "拖到状态栏空白的位置，可以更好的了解实时的网速情况，不用担心会影响" +
                                                "其他页面的视线。当然你想移动悬浮的时候，可以再次打开这个开关。")
                                        .create()
                                        .show()
                            },
                            switchButtonEnable = true,
                            switchStatus = isFixed,
                            onClickedListener = { isChecked ->
                                isFixed = isChecked
                                CustomViewManager.getInstance(this@MainActivity)?.
                                        ankoView?.isEnabled = !isChecked
                            })

                    view { backgroundColor = Color.parseColor("#dbdbdb") }
                            .lparams(matchParent, dip(1)) {
                                leftMargin = dip(16)
                                rightMargin = dip(16)
                            }

                    settingItem(
                            R.drawable.ic_no_touch,
                            R.drawable.ic_no_touch_white,
                            "可触摸",
                            settingStyle = SettingStyle.BOTTOM,
                            hintIconEnable = true,
                            hintClickListener = {
                                HintDialog.Builder(this@MainActivity)
                                        .content("可能这个开关和上一个在效果上有些重复。这个开关的作" +
                                                "用是是否响应用户的触摸事件，可能目前的只有移动这个功能。后面我" +
                                                "们可能加入更多有用的功能。敬请期待...")
                                        .create()
                                        .show()
                            },
                            switchButtonEnable = true,
                            switchStatus = isTouchable,
                            onClickedListener = { isChecked ->
                                isTouchable = isChecked
                                CustomViewManager.getInstance(this@MainActivity)?.
                                        setEnableTouch(isChecked)
                            })

                    space { }.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_permission,
                            R.drawable.ic_permission_white,
                            "通知监听权限",
                            hintIconEnable = true,
                            hintClickListener = {
                                HintDialog.Builder(this@MainActivity)
                                        .content("通知监听权限可以更方便的让我们根据判断通知的出现" +
                                                "隐藏网速显示的悬浮框，从而不会遮挡通知的显示预览。例如：" +
                                                "QQ消息，微信消息...")
                                        .create()
                                        .show()
                            },
                            itemClickListener = {
                                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                            })

                    space { }.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_permission,
                            R.drawable.ic_permission_white,
                            "反馈",
                            itemClickListener = {
                                startActivity(Intent(this@MainActivity,
                                        FeedbackActivity::class.java))
                            })
                }
            }
        }

        // 启动本地服务和远程服务
        if (commonROMPermissionCheck(this)) {
            startKeepService()
        } else {
            if (isM()) requestAlertWindowPermission()
        }
    }

    companion object {
        private val REQUEST_CODE = 1
    }

    /** 判断权限 */
    private fun commonROMPermissionCheck(context: Context): Boolean {
        var result = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val clazz = Settings::class.java
                val canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context::class.java)
                result = canDrawOverlays.invoke(null, context) == true
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return result
    }

    /** SDK版本是否是6.0 */
    private fun isM() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

    /** SDK版本是否是5.0 */
    private fun isL() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    /** 申请权限 */
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestAlertWindowPermission() {
        val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        intent.data = Uri.parse("package:" + packageName)
        startActivityForResult(intent, REQUEST_CODE)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (Settings.canDrawOverlays(this)) {
                startKeepService()
            }
        }
    }

    /**
     * 启动双服务
     */
    private fun startKeepService(){
        startService(Intent(this, LocalKeepService::class.java))
        startService(Intent(this, RemoteKeepService::class.java))
    }
}