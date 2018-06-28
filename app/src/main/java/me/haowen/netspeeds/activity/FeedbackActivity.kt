package me.haowen.netspeeds.activity

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import me.haowen.netspeeds.R
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.concurrent.TimeUnit

/**
 * author: yyhy
 * time  : 2017/10/31 16:04
 * desc  : 反馈页面
 */
class FeedbackActivity : AppCompatActivity() {

    private var tvContent: TextView? = null
    private val content = "如果你有什么好的想法或者意见，请联系我，我会尽我最大的努力去" +
            "实现和完善您们的要求。联系方式：xxx-xxx-xxx-xxx"
    private var dispose: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            backgroundColor = Color.parseColor("#ebebeb")

            relativeLayout {

                if (isL()) {
                    elevation = dip(3).toFloat()
                }

                backgroundColor = Color.parseColor("#f6f6f6")

                imageView {
                    backgroundResource = R.drawable.selector_btn_back
                    onClick { finish() }
                }.lparams(dip(24), dip(24)) {
                    leftMargin = dip(6)
                    alignParentLeft()
                    centerVertically()
                }

                textView("反馈") {
                    textSize = 20f
                    textColor = Color.parseColor("#616161")
                }.lparams(wrapContent, wrapContent) {
                    centerInParent()
                }

            }.lparams(matchParent, dip(48))

            scrollView {

                verticalLayout {

                    space { }.lparams(matchParent, dip(16))

                    tvContent = textView {
                        textSize = 16f
                        textColor = Color.parseColor("#8B8B8B")
                    }.lparams(wrapContent, wrapContent) {
                        margin = dip(16)
                    }
                }
            }
        }

        // 一个个字的显示文字
        dispose = Flowable.interval(400, 250, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onBackpressureDrop()
                .subscribe({ index ->
                    if (index in 0..content.length) tvContent?.text = content.substring(0, index.toInt())
                }, {})
    }

    /** SDK版本是否是5.0 */
    private fun isL() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }
}