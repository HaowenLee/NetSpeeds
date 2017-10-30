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
import android.widget.ImageView
import android.widget.LinearLayout
import me.haowen.netspeeds.R
import me.haowen.netspeeds.global.PreKey
import me.haowen.netspeeds.service.FloatService
import me.haowen.netspeeds.util.Preference
import me.haowen.netspeeds.widget.CustomViewManager
import me.haowen.netspeeds.widget.settingItem
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick


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

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                backgroundResource = R.drawable.sl_setting_item_single

                onClick {
                    val top = IntArray(2)
                    CustomViewManager.getInstance(this@MainActivity)?.ankoView?.
                            getLocationOnScreen(top)
                    screenX = top[0]
                    screenY = top[1]
                }

                frameLayout {
                    //                    backgroundResource = R.drawable.shape_setting_item_single_left

                    imageView {
                        imageResource = R.drawable.ic_save
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(90, 90) {
                        gravity = Gravity.CENTER
                    }
                }.lparams(172, 172)

                view {
                    backgroundColor = Color.parseColor("#e8e8e8")
                }.lparams(2, matchParent)

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
//                    backgroundResource = R.drawable.shape_setting_item_single_right

                    textView("记忆位置") {
                        textSize = 18f
                        textColor = Color.parseColor("#333333")
                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = 42
                    }

                    space {}.lparams(0, matchParent) {
                        weight = 1f
                    }

                    imageView {
                        imageResource = R.drawable.ic_right
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(72, 72) {
                        gravity = Gravity.CENTER
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(15)
                rightMargin = dip(15)
            }

            space {}.lparams(matchParent, dip(16))

            verticalLayout {
                backgroundResource = R.drawable.shape_setting_item_normal

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL

                    frameLayout {
                        backgroundResource = R.drawable.shape_setting_item_top_left

                        imageView {
                            imageResource = R.drawable.ic_pushpin
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(90, 90) {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(172, 172)

                    view {
                        backgroundColor = Color.parseColor("#e8e8e8")
                    }.lparams(2, matchParent)

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundResource = R.drawable.shape_setting_item_top_right

                        textView("固定在屏幕的该位置") {
                            textSize = sp(4f).toFloat()
                            textColor = Color.parseColor("#8b8b8b")
                        }.lparams(wrapContent, wrapContent) {
                            leftMargin = 42
                        }

                        imageView {
                            imageResource = R.drawable.ic_info
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(dip(21), dip(21)) {
                            gravity = Gravity.CENTER
                            leftMargin = 20
                        }

                        space {}.lparams(0, matchParent) {
                            weight = 1f
                        }

                        switch {
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
                        }.lparams(wrapContent, wrapContent) {
                            rightMargin = dip(13)
                        }
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, wrapContent) {
                    padding = dip(1)
                }

                view {
                    backgroundColor = Color.parseColor("#ebebeb")
                }.lparams(matchParent, 2)

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL

                    frameLayout {
                        backgroundResource = R.drawable.shape_setting_item_bottom_left

                        imageView {
                            imageResource = R.drawable.ic_no_touch
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(90, 90) {
                            gravity = Gravity.CENTER
                        }
                    }.lparams(172, 172)

                    view {
                        backgroundColor = Color.parseColor("#e8e8e8")
                    }.lparams(2, matchParent)

                    linearLayout {
                        orientation = LinearLayout.HORIZONTAL
                        gravity = Gravity.CENTER_VERTICAL
                        backgroundResource = R.drawable.shape_setting_item_bottom_right

                        textView("不再接收触摸事件") {
                            textSize = sp(4f).toFloat()
                            textColor = Color.parseColor("#8b8b8b")
                        }.lparams(wrapContent, wrapContent) {
                            leftMargin = 42
                        }

                        imageView {
                            imageResource = R.drawable.ic_info
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }.lparams(dip(21), dip(21)) {
                            gravity = Gravity.CENTER
                            leftMargin = 20
                        }

                        space {}.lparams(0, matchParent) {
                            weight = 1f
                        }

                        switch {
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
                        }.lparams(wrapContent, wrapContent) {
                            rightMargin = dip(13)
                        }
                    }.lparams(matchParent, matchParent)
                }.lparams(matchParent, wrapContent)
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(15)
                rightMargin = dip(15)
            }

            space { }.lparams(matchParent, dip(16))

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                backgroundResource = R.drawable.sl_setting_item_single

                onClick {
                    startActivity(Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"))
                }

                frameLayout {
                    //                    backgroundResource = R.drawable.shape_setting_item_single_left

                    imageView {
                        imageResource = R.drawable.ic_permission
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(90, 90) {
                        gravity = Gravity.CENTER
                    }
                }.lparams(172, 172)

                view {
                    backgroundColor = Color.parseColor("#e8e8e8")
                }.lparams(2, matchParent)

                linearLayout {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER_VERTICAL
//                    backgroundResource = R.drawable.shape_setting_item_single_right

                    textView("通知监听权限") {
                        textSize = 18f
                        textColor = Color.parseColor("#333333")
                    }.lparams(wrapContent, wrapContent) {
                        leftMargin = 42
                    }

                    space {}.lparams(0, matchParent) {
                        weight = 1f
                    }

                    imageView {
                        imageResource = R.drawable.ic_right
                        scaleType = ImageView.ScaleType.CENTER_CROP
                    }.lparams(72, 72) {
                        gravity = Gravity.CENTER
                        rightMargin = dip(15)
                    }
                }.lparams(matchParent, matchParent)
            }.lparams(matchParent, wrapContent) {
                leftMargin = dip(15)
                rightMargin = dip(15)
            }

            settingItem {

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