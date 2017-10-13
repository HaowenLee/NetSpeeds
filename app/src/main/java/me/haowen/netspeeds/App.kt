package me.haowen.netspeeds

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho

/**
 * author: yyhy
 * date  : 2017/10/10 17:44
 * desc  : App
 */
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
        lateinit var sharePre: SharedPreferences
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        Stetho.initializeWithDefaults(this)
        sharePre = getSharedPreferences("config", Context.MODE_PRIVATE)
    }
}
