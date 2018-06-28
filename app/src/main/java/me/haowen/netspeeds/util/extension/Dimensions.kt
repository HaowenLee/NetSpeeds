package me.haowen.netspeeds.util.extension

import android.view.View
import me.haowen.netspeeds.util.ScreenUtil

/**
 * ================================================
 * 作    者：Herve、Li
 * 版    本：1.0
 * 创建日期：2018/3/6
 * 描    述：
 * 修订历史：
 * ================================================
 */

/**
 * 像素转换
 *
 * @param value 720x1280 设计稿 的像素
 */
inline fun View.px720(value: Int): Int = (value.toFloat() / 720 * ScreenUtil.screenWidth).toInt()