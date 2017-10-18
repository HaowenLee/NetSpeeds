package me.haowen.netspeeds.util

import android.content.Context
import android.content.SharedPreferences
import me.haowen.netspeeds.global.App
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class Preference<T>(private val name: String?, private val default: T) : ReadWriteProperty<Any?, T> {

    private val sharePre: SharedPreferences = App.context!!.getSharedPreferences("config",
            Context.MODE_PRIVATE)

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T = with(sharePre) {
        val res: Any = when (default) {
            is Long -> {
                getLong(name, 0)
            }
            is String -> {
                getString(name, default)
            }
            is Float -> {
                getFloat(name, default)
            }
            is Int -> {
                getInt(name, default)
            }
            is Boolean -> {
                getBoolean(name, default)
            }
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }
        res as T
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) = with(sharePre.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Float -> putFloat(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            else -> {
                throw IllegalArgumentException("This type can't be saved into Preferences")
            }
        }.apply()
    }
}