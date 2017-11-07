package me.haowen.netspeeds.service

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import app.project.KeepProgressConnection
import me.haowen.netspeeds.widget.CustomViewManager


/**
 * author: yyhy
 * time  : 2017/11/7 14:52
 * desc  : 远程进程
 */
class RemoteKeepService : Service() {

    private var myBinder: MyBinder? = null
    private var myServiceConnection: MyServiceConnection? = null

    override fun onCreate() {
        super.onCreate()
        if (myBinder == null) {
            myBinder = MyBinder()
        }
        if (myServiceConnection == null) {
            myServiceConnection = MyServiceConnection()
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        this.bindService(Intent(this, LocalKeepService::class.java),
                myServiceConnection, Context.BIND_IMPORTANT)
        return Service.START_STICKY
    }

    private inner class MyServiceConnection : ServiceConnection {
        override fun onServiceConnected(arg0: ComponentName, arg1: IBinder) {
            Log.i("Keep", "远程服务连接成功")
            // 当远程服务没有显示的时候本地
            val asInterface = KeepProgressConnection.Stub.asInterface(arg1)
            if (asInterface != null) {
                // 当远程服务没有显示的时候
                if (!asInterface.isAlreadyShowView) {
                    myBinder?.isShow = true
                    myBinder?.showFloatView()
                }
            }
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            // 连接出现了异常断开了，LocalKeepService被杀死了
            Toast.makeText(this@RemoteKeepService, "远程服务Remote被干掉", Toast.LENGTH_LONG).show()
            // 启动LocalKeepService
            this@RemoteKeepService.startService(Intent(this@RemoteKeepService, LocalKeepService::class.java))
            this@RemoteKeepService.bindService(Intent(this@RemoteKeepService, LocalKeepService::class.java),
                    myServiceConnection, Context.BIND_IMPORTANT)
        }
    }

    internal inner class MyBinder : KeepProgressConnection.Stub() {

        /** 是否已经显示 */
        var isShow = false

        override fun isAlreadyShowView(): Boolean = isShow

        override fun getServiceName(): String = "Remote"


        fun showFloatView() {
            CustomViewManager.getInstance(this@RemoteKeepService)?.showFloatViewOnWindow()
        }
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return myBinder
    }
}