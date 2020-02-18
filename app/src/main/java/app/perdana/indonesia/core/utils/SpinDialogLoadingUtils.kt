package app.perdana.indonesia.core.utils

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.view.WindowManager
import app.perdana.indonesia.R

/**
 * Created by ebysofyan on 2020-01-30.
 */
object SpinDialogLoadingUtils {
    fun showSpinDialogLoading(context: Context): Dialog {
        val dialog = Dialog(context).apply {
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.spinner_loading)
            setCancelable(false)
        }

        try {
            val layoutParams = WindowManager.LayoutParams().apply {
                copyFrom(dialog.window?.attributes)
                width = WindowManager.LayoutParams.MATCH_PARENT
                height = WindowManager.LayoutParams.WRAP_CONTENT
            }
            dialog.window?.attributes = layoutParams
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dialog
    }
}