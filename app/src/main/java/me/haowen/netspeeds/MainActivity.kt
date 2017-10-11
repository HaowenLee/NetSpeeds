package me.haowen.netspeeds

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import org.jetbrains.anko.relativeLayout
import org.jetbrains.anko.textColor
import org.jetbrains.anko.textView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        relativeLayout {

            textView {
                textSize = 32f
                textColor = Color.BLACK
                setText(R.string.app_name)
            }.lparams {
                gravity = Gravity.CENTER
            }
        }

        startService(Intent(this, FloatService::class.java))
    }
}