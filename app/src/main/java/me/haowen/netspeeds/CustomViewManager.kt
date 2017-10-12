package me.haowen.netspeeds

import NetworkUtil
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.*
import android.widget.TextView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.textColor
import java.util.concurrent.TimeUnit


class CustomViewManager private constructor(private val mContext: Context) {

    //窗口管理类
    private val mWindowManager: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    val ankoView: TextView

    private val hideView: View

    init {
        ankoView = initView()
        hideView = View(mContext)
    }

    private fun initView(): TextView = TextView(mContext).apply {
        textSize = 14f
        textColor = Color.WHITE
//        setBackgroundResource(R.drawable.floatview_background)
        text = "0kb/s"
        gravity = Gravity.CENTER
        layoutParams = ViewGroup.LayoutParams(100, 100)
    }

    private val parmas = WindowManager.LayoutParams()

    /**
     * @param
     * @description 在手机屏幕上显示自定义的FloatView
     * @author ldm
     * @time 2016/8/17 13:47
     */
    fun showFloatViewOnWindow() {
        val screenX by Preference(PreKey.SCREEN_X, 0)
        val screenY by Preference(PreKey.SCREEN_Y, 0)

        parmas.width = 200
        parmas.height = 68
        //窗口图案放置位置
        parmas.gravity = Gravity.TOP or Gravity.LEFT
        // 如果忽略gravity属性，那么它表示窗口的绝对X位置。
        parmas.x = screenX
        //如果忽略gravity属性，那么它表示窗口的绝对Y位置。
        parmas.y = screenY

        ////电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
        parmas.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        //FLAG_NOT_FOCUSABLE让window不能获得焦点，这样用户快就不能向该window发送按键事件及按钮事件
        //FLAG_NOT_TOUCH_MODAL即使在该window在可获得焦点情况下，仍然把该window之外的任何event发送到该window之后的其他window.
        parmas.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
        // 期望的位图格式。默认为不透明。参考android.graphics.PixelFormat。
        parmas.format = PixelFormat.RGBA_8888
        mWindowManager.addView(ankoView, parmas)

        addHideView()

        initListener()

        Flowable.interval(0, 3, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    val top = IntArray(2)

                    // TODO 横全屏到竖屏的时候获取不准确
                    hideView.getLocationOnScreen(top)

                    if (top[1] == 0) {
                        ankoView.visibility = View.GONE
                    } else {
                        ankoView.visibility = View.VISIBLE
                        ankoView.text = (NetworkUtil.getNetSpeed())
                    }
                }, { e ->
                    e.printStackTrace()
                })
    }

    private fun addHideView() {
        val hideParams = WindowManager.LayoutParams()
        hideParams.width = 100
        hideParams.height = 100
        //窗口图案放置位置
        hideParams.gravity = Gravity.TOP or Gravity.LEFT
        // 如果忽略gravity属性，那么它表示窗口的绝对X位置。
        hideParams.x = 0
        //如果忽略gravity属性，那么它表示窗口的绝对Y位置。
        hideParams.y = 0
        ////电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
        hideParams.type = WindowManager.LayoutParams.TYPE_PHONE
        //FLAG_NOT_FOCUSABLE让window不能获得焦点，这样用户快就不能向该window发送按键事件及按钮事件
        //FLAG_NOT_TOUCH_MODAL即使在该window在可获得焦点情况下，仍然把该window之外的任何event发送到该window之后的其他window.
        hideParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        // 期望的位图格式。默认为不透明。参考android.graphics.PixelFormat。
        hideParams.format = PixelFormat.RGBA_8888
        mWindowManager.addView(hideView, hideParams)
    }

    private fun initListener() {

        var mX = 0
        var mY = 0

        val isFixed by Preference(PreKey.IS_FIXED, false)

        ankoView.isEnabled = !isFixed

        (ankoView as View).setOnTouchListener { v, event ->

            when (event.action) {

                MotionEvent.ACTION_DOWN -> {
                    mX = event.rawX.toInt()
                    mY = event.rawY.toInt()
                }

                MotionEvent.ACTION_MOVE -> {
                    val tempX = event.rawX.toInt()
                    val tempY = event.rawY.toInt()
                    parmas.x += (tempX - mX)
                    parmas.y += (tempY - mY)
                    mX = tempX
                    mY = tempY
                    mWindowManager.updateViewLayout(v, parmas)
                }
            }

            true
        }
    }

    companion object {

        //本类实例
        @SuppressLint("StaticFieldLeak")
        private var instance: CustomViewManager? = null

        /**
         * @param
         * @description 通过单例模式获取实例对象
         * @author ldm
         * @time 2016/8/17 11:59
         */
        fun getInstance(mContext: Context): CustomViewManager? {
            if (null == instance) {
                synchronized(CustomViewManager::class.java) {
                    if (null == instance) {
                        instance = CustomViewManager(mContext)
                    }
                }
            }
            return instance
        }
    }
}