package com.example.inline_everything

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        button_crash.setOnClickListener { crash { "boom" } }
        button_toast.setOnClickListener { toast { "lol" } }
        button_slow_crash.setOnClickListener { crashSlowly() }
        button_slow_toast.setOnClickListener { toastSlowly(this) }
        button_inline_crash.setOnClickListener {
            InlineClassBecauseWhyNot("a")
                .crash()
        }
        button_inline_inline_crash.setOnClickListener {
            InlineClassBecauseWhyNot("b")
                .crashExternally()
        }
    }
}

inline fun Context.toast(duration: Int = Toast.LENGTH_LONG, text: () -> String) {
    Toast.makeText(this, text(), duration).show()
}

inline fun crash(text: () -> String) {
    throw Throwable(text())
}

inline fun startNewThreadAndRun(crossinline task: () -> Unit) {
    Thread(Runnable {
        Thread.sleep(1000)
        task()
    }).start()
}

inline fun toastSlowly(activity: Activity) {
    startNewThreadAndRun {
        activity.runOnUiThread {
            activity.toast { "lol slowly" }
        }
    }
}

inline fun crashSlowly() {
    startNewThreadAndRun {
        crash { "boom slowly" }
    }
}

inline class InlineClassBecauseWhyNot(val text: String) {
    fun crash() {
        crash { "boom internally $text" }
    }
}

inline fun InlineClassBecauseWhyNot.crashExternally() {
    crash { "boom externally $this" }
}
