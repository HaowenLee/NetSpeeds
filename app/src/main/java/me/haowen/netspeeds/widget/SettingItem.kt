package me.haowen.netspeeds.widget

import android.content.Context
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.LinearLayout

/**
 * author: yyhy
 * time  : 2017/10/26 10:21
 * desc  : 设置的Item
 */
class SettingItem @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    @DrawableRes
    var icon: Int = 0

    init {
        orientation = HORIZONTAL
    }
}