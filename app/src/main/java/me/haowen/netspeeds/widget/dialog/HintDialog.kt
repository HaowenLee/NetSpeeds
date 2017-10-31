package me.haowen.netspeeds.widget.dialog

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatDialog
import android.widget.TextView
import me.haowen.netspeeds.R

class HintDialog(context: Context) : AppCompatDialog(context) {

    private var mContent: CharSequence = ""
    private var tvContent: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_hint)

        tvContent = findViewById(R.id.tvContent)
    }

    override fun show() {
        super.show()

        if (mContent.isNotEmpty()) {
            tvContent?.text = mContent
        }
    }

    class Builder(context: Context) {

        private val mHintDialog = HintDialog(context)

        fun content(content: CharSequence): Builder {
            mHintDialog.mContent = content
            return this
        }

        fun create() = mHintDialog
    }
}