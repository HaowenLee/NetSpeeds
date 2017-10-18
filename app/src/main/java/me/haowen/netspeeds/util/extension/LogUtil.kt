package me.haowen.netspeeds.util.extension

import android.util.Log

val debugMode = true

inline fun <reified T> T.debug(log: Any?) {
    if (debugMode) Log.d(T::class.simpleName, log.toString())
}

inline fun <reified T> T.error(log: Any?) {
    if (debugMode) Log.e(T::class.simpleName, log.toString())
}

inline fun <reified T> T.info(log: Any?) {
    if (debugMode) Log.i(T::class.simpleName, log.toString())
}

fun Any.debug(tag: Any?, log: Any?) {
    if (debugMode) Log.d(tag.toString(), log.toString())
}

fun Any.error(tag: Any?, log: Any?) {
    if (debugMode) Log.e(tag.toString(), log.toString())
}

fun Any.info(tag: Any?, log: Any?) {
    if (debugMode) Log.i(tag.toString(), log.toString())
}