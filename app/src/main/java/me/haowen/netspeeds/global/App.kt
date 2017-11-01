package me.haowen.netspeeds.global

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.facebook.stetho.Stetho
import me.haowen.netspeeds.global.App.Companion.context

/**
 * The Application of this project.
 *
 * @author haowen
 * @property context context of base Application
 */
class App : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        lateinit var sharePre: SharedPreferences
    }

    /**
     * When Application is created, It will be called.
     */
    override fun onCreate() {
        super.onCreate()
        context = this
        Stetho.initializeWithDefaults(this)
        sharePre = getSharedPreferences("config", Context.MODE_PRIVATE)
    }
}
