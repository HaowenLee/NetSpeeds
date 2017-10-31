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
import me.haowen.netspeeds.service.FloatService
import me.haowen.netspeeds.util.Preference
import me.haowen.netspeeds.widget.CustomViewManager
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
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                elevation = dip(3).toFloat()

                backgroundColor = Color.parseColor("#f6f6f6")

                textView("设置") {
                    textSize = 20f
                    textColor = Color.parseColor("#616161")
                }.lparams {
                    gravity = Gravity.CENTER
                }

            }.lparams(matchParent, 156)

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
                    })

            scrollView {

                verticalLayout {

                    space {}.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_pushpin,
                            R.drawable.ic_pushpin_white,
                            "固定",
                            hintIconEnable = true,
                            hintClickListener = {

                            },
                            switchButtonEnable = true,
                            switchStatus = isFixed,
                            onClickedListener = { isChecked ->
                                isFixed = isChecked
                                CustomViewManager.getInstance(this@MainActivity)?.
                                        ankoView?.isEnabled = !isChecked
                            })

                    settingItem(
                            R.drawable.ic_no_touch,
                            R.drawable.ic_no_touch_white,
                            "可触摸",
                            hintIconEnable = true,
                            hintClickListener = {

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

                            },
                            itemClickListener = {
                                startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                            })

                    space { }.lparams(matchParent, dip(16))

                    settingItem(
                            R.drawable.ic_permission,
                            R.drawable.ic_permission_white,
                            "反馈",
                            hintIconEnable = true,
                            itemClickListener = {
                            })
                }
            }
        }

        if (commonROMPermissionCheck(this)) {
            startService(Intent(this, FloatService::class.java))
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
                startService(Intent(this, FloatService::class.java))
            }
        }
    }
}