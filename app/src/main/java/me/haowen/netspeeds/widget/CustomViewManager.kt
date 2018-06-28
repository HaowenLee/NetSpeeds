package me.haowen.netspeeds.widget

import BarUtil
import NetworkUtil
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.Build
import android.util.TypedValue
import android.view.*
import android.widget.TextView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.haowen.netspeeds.global.PreKey
import me.haowen.netspeeds.util.Preference
import me.haowen.netspeeds.util.ScreenUtil
import org.jetbrains.anko.textColor
import java.util.concurrent.TimeUnit


class CustomViewManager private constructor(private val mContext: Context) {

    /** 窗口管理类 */
    private val mWindowManager: WindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    val ankoView: TextView

    private val hideView: View

    /** 状态栏高度 */
    private val barHeight by lazy { BarUtil.getStatusBarHeight(mContext) }

    /** 文字大小 */
    private val mTextSize by lazy { barHeight / 9f * 5f }

    private var flowable: Flowable<Long>? = Flowable.interval(0, 2, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureDrop()

    private var disposable: Disposable? = null

    init {
        ankoView = initView()
        hideView = View(mContext)
    }

    private fun initView(): TextView = TextView(mContext).apply {
        setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize)
        textColor = Color.WHITE
        text = "0KB"
        gravity = Gravity.CENTER
        layoutParams = ViewGroup.LayoutParams(100, 100)
    }

    private val lp = WindowManager.LayoutParams()

    fun setEnableTouch(enable: Boolean, update: Boolean = true) {
        if (enable) {
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        } else {
            lp.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        }
        if (update) {
            mWindowManager.updateViewLayout(ankoView, lp)
        }
    }

    var isVisible = true

    /**
     * @param
     * @description 在手机屏幕上显示自定义的FloatView
     * @author ldm
     * @time 2016/8/17 13:47
     */
    @Synchronized
    fun showFloatViewOnWindow() {
        val screenX by Preference(PreKey.SCREEN_X, 0)
        val screenY by Preference(PreKey.SCREEN_Y, 0)
        val isTouchable by Preference(PreKey.IS_TOUCHABLE, true)

        lp.width = 200
        lp.height = barHeight
        //窗口图案放置位置
        lp.gravity = Gravity.TOP or Gravity.LEFT
        // 如果忽略gravity属性，那么它表示窗口的绝对X位置。
        lp.x = ScreenUtil.screenWidth / 4
        //如果忽略gravity属性，那么它表示窗口的绝对Y位置。
        lp.y = 0

        ////电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR
        }
        //FLAG_NOT_FOCUSABLE让window不能获得焦点，这样用户快就不能向该window发送按键事件及按钮事件
        //FLAG_NOT_TOUCH_MODAL即使在该window在可获得焦点情况下，仍然把该window之外的任何event发送到该window之后的其他window.

        setEnableTouch(isTouchable, false)

        // 期望的位图格式。默认为不透明。参考android.graphics.PixelFormat。
        lp.format = PixelFormat.RGBA_8888

        try {
            mWindowManager.addView(ankoView, lp)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        addHideView()

        initListener()

        if (disposable == null || disposable?.isDisposed == true) {
            disposable = flowable?.subscribe({
                val top = IntArray(2)
                // TODO 横全屏到竖屏的时候获取不准确
                hideView.getLocationOnScreen(top)

                if (ScreenUtil.screenWidth < ScreenUtil.screenHeight) {
                    if (!isVisible) {
                        ankoView.visibility = View.GONE
                    } else {
                        if (top[1] == 0) {
                            ankoView.visibility = View.GONE
                        } else {
                            ankoView.visibility = View.VISIBLE
                            ankoView.text = (NetworkUtil.getNetSpeed())
                        }
                    }
                } else { // 横屏模式下不显示
                    ankoView.visibility = View.GONE
                }
            }, { e -> e.printStackTrace() })
        }
    }

    private fun addHideView() {
        val hideParams = WindowManager.LayoutParams()
        hideParams.width = 0
        hideParams.height = 0
        //窗口图案放置位置
        hideParams.gravity = Gravity.TOP or Gravity.LEFT
        // 如果忽略gravity属性，那么它表示窗口的绝对X位置。
        hideParams.x = 0
        //如果忽略gravity属性，那么它表示窗口的绝对Y位置。
        hideParams.y = 0
        ////电话窗口。它用于电话交互（特别是呼入）。它置于所有应用程序之上，状态栏之下。
        hideParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        //FLAG_NOT_FOCUSABLE让window不能获得焦点，这样用户快就不能向该window发送按键事件及按钮事件
        //FLAG_NOT_TOUCH_MODAL即使在该window在可获得焦点情况下，仍然把该window之外的任何event发送到该window之后的其他window.
        hideParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        // 期望的位图格式。默认为不透明。参考android.graphics.PixelFormat。
        hideParams.format = PixelFormat.RGBA_8888

        try {
            mWindowManager.addView(hideView, hideParams)
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
                    lp.x += (tempX - mX)
                    lp.y += (tempY - mY)
                    mX = tempX
                    mY = tempY
                    mWindowManager.updateViewLayout(v, lp)
                }
            }

            true
        }
    }

    companion object {

        /** 本类实例 */
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var instance: CustomViewManager? = null

        /**
         * @param
         * @description 通过单例模式获取实例对象
         * @author lhw
         * @time 2016/8/17 11:59
         */
        @Synchronized
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