package app.perdana.indonesia.core.extension

import android.annotation.SuppressLint
import android.view.KeyEvent
import android.view.MotionEvent
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText

@SuppressLint("ClickableViewAccessibility")
fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

@SuppressLint("ClickableViewAccessibility")
fun TextInputEditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
    this.setOnTouchListener { v, event ->
        var hasConsumed = false
        if (v is EditText) {
            if (event.x >= v.width - v.totalPaddingRight) {
                if (event.action == MotionEvent.ACTION_UP) {
                    onClicked(this)
                }
                hasConsumed = true
            }
        }
        hasConsumed
    }
}

fun TextInputEditText.wrapText() {
    if (this.hasFocus()) {
        this.setOnKeyListener { _, keyCode, _ ->
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                this.text?.append("\n")
            }
            return@setOnKeyListener false
        }
    }
}

fun TextInputEditText.validatePhone(): Boolean = this.text?.toString()?.startsWith("0") ?: false || this.text?.toString()?.startsWith("+62") ?: false

fun EditText.validatePhone(): Boolean = this.text?.toString()?.startsWith("0") ?: false || this.text?.toString()?.startsWith("+62") ?: false