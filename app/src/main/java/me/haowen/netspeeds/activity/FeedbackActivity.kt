package me.haowen.netspeeds.activity

import android.annotation.TargetApi
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import org.jetbrains.anko.*

/**
 * author: yyhy
 * time  : 2017/10/31 16:04
 * desc  : 反馈页面
 */
class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            backgroundColor = Color.parseColor("#ebebeb")

            relativeLayout {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                elevation = dip(3).toFloat()

                backgroundColor = Color.parseColor("#f6f6f6")

                textView("反馈") {
                    textSize = 20f
                    textColor = Color.parseColor("#616161")
                }.lparams {
                    gravity = Gravity.CENTER
                }

            }.lparams(matchParent, 156)

            scrollView {

                verticalLayout {

                    space { }.lparams(matchParent, dip(16))


                }
            }
        }
    }
}