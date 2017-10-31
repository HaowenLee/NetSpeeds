package me.haowen.netspeeds.widget

import android.graphics.Color
import android.support.annotation.DrawableRes
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import me.haowen.netspeeds.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onCheckedChange
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sdk25.coroutines.onTouch

object SettingStyle {
    const val TOP = 1
    const val MIDDLE = 2
    const val BOTTOM = 3
    const val SINGLE = 4
}

fun ViewManager.settingItem(@DrawableRes iconNormalRes: Int,
                            @DrawableRes iconPressedRes: Int,
                            title: String,
                            settingStyle: Int = SettingStyle.SINGLE,
                            hintIconEnable: Boolean = false,
                            hintClickListener: ((Unit) -> Unit)? = null,
                            itemClickListener: ((Unit) -> Unit)? = null,
                            switchButtonEnable: Boolean = false,
                            switchStatus: Boolean = false,
                            onClickedListener: ((isChecked: Boolean) -> Unit)? = null) = linearLayout {

    orientation = LinearLayout.HORIZONTAL
    backgroundResource = when (settingStyle) {
        SettingStyle.TOP -> R.drawable.shape_setting_item_top
        SettingStyle.MIDDLE -> R.drawable.shape_setting_item_middle
        SettingStyle.BOTTOM -> R.drawable.shape_setting_item_bottom
        else -> R.drawable.shape_setting_item_normal
    }
    gravity = Gravity.CENTER_VERTICAL

    lparams(matchParent, wrapContent) {
        leftMargin = dip(16)
        rightMargin = dip(16)
    }

    var ivIcon: ImageView? = null

    val frame = frameLayout {
        backgroundResource = R.drawable.shape_setting_item_single_left

        ivIcon = imageView {
            imageResource = iconNormalRes
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.lparams(dip(30), dip(30)) {
            gravity = Gravity.CENTER
        }
    }.lparams(dip(56), dip(56)) {
        leftPadding = dip(1)
        rightPadding = dip(1)
        when (settingStyle) {
            SettingStyle.TOP -> {
                topPadding = dip(1)
            }
            SettingStyle.MIDDLE -> {

            }
            SettingStyle.BOTTOM -> {
                bottomPadding = dip(1)
            }
            else -> {
                topPadding = dip(1)
                bottomPadding = dip(1)
            }
        }
    }

    view {
        backgroundColor = Color.parseColor("#e8e8e8")
    }.lparams(2, matchParent)

    val tvName = textView(title) {
        textSize = 18f
        textColor = Color.parseColor("#333333")
    }.lparams(wrapContent, wrapContent) {
        leftMargin = dip(15)
    }

    var ivHint: ImageView? = null
    if (hintIconEnable) {
        ivHint = imageView {
            imageResource = R.drawable.sl_hint
            scaleType = ImageView.ScaleType.CENTER_CROP

            onClick { hintClickListener?.invoke(Unit) }
        }.lparams(dip(21), dip(21)) {
            leftMargin = dip(8)
        }
    }

    space {}.lparams(0, matchParent) {
        weight = 1f
    }

    var ivRight: ImageView? = null

    if (switchButtonEnable) {
        switch {
            isChecked = switchStatus
            onCheckedChange { _, isChecked -> onClickedListener?.invoke(isChecked) }
        }.lparams(wrapContent, wrapContent) {
            rightMargin = dip(13)
        }
    } else {
        ivRight = imageView {
            imageResource = R.drawable.ic_right
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.lparams(dip(24), dip(24f)) {
            rightMargin = dip(15)
        }

        onClick { itemClickListener?.invoke(Unit) }

        onTouch(returnValue = false) { _, event ->

            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    backgroundResource = when (settingStyle) {
                        SettingStyle.TOP -> R.drawable.shape_setting_item_top
                        SettingStyle.MIDDLE -> R.drawable.shape_setting_item_middle
                        SettingStyle.BOTTOM -> R.drawable.shape_setting_item_bottom
                        else -> R.drawable.shape_setting_item_normal
                    }
                    frame.setBackgroundResource(R.drawable.shape_setting_item_single_left)
                    tvName.setTextColor(Color.parseColor("#333333"))
                    ivHint?.setImageResource(R.drawable.sl_hint)
                    ivRight?.setImageResource(R.drawable.ic_right)
                    ivIcon?.setImageResource(iconNormalRes)
                }
                MotionEvent.ACTION_DOWN -> {
                    backgroundResource = when (settingStyle) {
                        SettingStyle.TOP -> R.drawable.shape_setting_item_top_pressed
                        SettingStyle.MIDDLE -> R.drawable.shape_setting_item_middle_pressed
                        SettingStyle.BOTTOM -> R.drawable.shape_setting_item_bottom_pressed
                        else -> R.drawable.shape_setting_item_single_pressed
                    }
                    frame.setBackgroundResource(0)
                    tvName.setTextColor(Color.parseColor("#ffffff"))
                    ivHint?.setImageResource(R.drawable.ic_hint_white)
                    ivRight?.setImageResource(R.drawable.ic_right_white)
                    ivIcon?.setImageResource(iconPressedRes)
                }
            }
        }
    }
}