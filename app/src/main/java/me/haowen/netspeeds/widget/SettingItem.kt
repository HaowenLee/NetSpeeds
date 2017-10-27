package me.haowen.netspeeds.widget

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.ViewManager
import android.widget.ImageView
import android.widget.LinearLayout
import me.haowen.netspeeds.R
import org.jetbrains.anko.*

open class SettingItemLayout(ctx: Context) : _LinearLayout(ctx)

inline fun ViewManager.settingItem() = linearLayout {

    orientation = LinearLayout.HORIZONTAL
    backgroundResource = R.drawable.shape_setting_item_normal
    gravity = Gravity.CENTER_VERTICAL

    lparams(matchParent, wrapContent) {
        leftMargin = dip(16)
        rightMargin = dip(16)
    }

    frameLayout {
        backgroundResource = R.drawable.shape_setting_item_single_left

        imageView {
            imageResource = R.drawable.ic_save
            scaleType = ImageView.ScaleType.CENTER_CROP
        }.lparams(dip(30), dip(30)) {
            gravity = Gravity.CENTER
        }
    }.lparams(dip(56), dip(56)) {
        padding = dip(1)
    }

    view {
        backgroundColor = Color.parseColor("#e8e8e8")
    }.lparams(2, matchParent)

    textView("记忆位置") {
        textSize = 18f
        textColor = Color.parseColor("#333333")
    }.lparams(wrapContent, wrapContent) {
        leftMargin = dip(15)
    }

    imageView {
        imageResource = R.drawable.ic_info
        scaleType = ImageView.ScaleType.CENTER_CROP
    }.lparams(dip(21), dip(21)) {
        leftMargin = dip(8)
    }

    space {}.lparams(0, matchParent) {
        weight = 1f
    }

    imageView {
        imageResource = R.drawable.ic_right
        scaleType = ImageView.ScaleType.CENTER_CROP
    }.lparams(dip(24), dip(24f)) {
        rightMargin = dip(15)
    }
}