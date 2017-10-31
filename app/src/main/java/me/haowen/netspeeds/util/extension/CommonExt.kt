package me.haowen.netspeeds.util.extension

import android.graphics.drawable.Drawable
import android.support.annotation.*
import me.haowen.netspeeds.global.App

/**
 * Use the function name beginning with 'I' to avoid the name of the original function.
 */

private fun getResources() = App.context.resources

fun Any.getIString(@StringRes resId: Int): String = getResources().getString(resId)

fun Any.getIStringArray(@ArrayRes resId: Int): Array<String> = getResources().getStringArray(resId)

fun Any.getIDimens(@DimenRes resId: Int): Int = getResources().getDimensionPixelSize(resId)

@Suppress("DEPRECATION")
fun Any.getIColor(@ColorRes resId: Int): Int = getResources().getColor(resId)

@Suppress("DEPRECATION")
fun Any.getIDrawable(@DrawableRes drawableRes: Int): Drawable = getResources().getDrawable(drawableRes)

fun Any.getIInteger(@IntegerRes integerRes: Int): Int = getResources().getInteger(integerRes)

fun Any.getIIdentifier(name: String, defType: String): Int = getResources().getIdentifier(name, defType, App.context.packageName)