package app.perdana.indonesia.core.utils

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by ebysofyan on 12/23/19.
 */

object ProgressDialogHelper {
    private var progressDialog: ProgressDialog? = null
    fun getInstance(context : Context, message: String = "Loading . . ."): ProgressDialog? {
        synchronized(ProgressDialogHelper::class.java){
            if (progressDialog == null) {
                progressDialog = ProgressDialog(context)
            }
            progressDialog?.apply {
                setTitle("")
                setMessage(message)
                isIndeterminate = true
            }
        }
        return progressDialog
    }
}