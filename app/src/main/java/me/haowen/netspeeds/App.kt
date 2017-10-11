package me.haowen.netspeeds

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * author: yyhy
 * date: 2017/10/10 17:44
 * desc:
 */
class App : Application() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        var context: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }
}
