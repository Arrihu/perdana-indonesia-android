package app.perdana.indonesia.core.extension

import android.util.Log
import app.perdana.indonesia.core.base.PerdanaError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

/**
 * Created by ebysofyan on 12/13/19.
 */

fun ResponseBody.getErrorDetail(): String {
    val type = object : TypeToken<PerdanaError>() {}.type
    val error = Gson().fromJson<PerdanaError>(this.charStream(), type)
    Log.e("getErrorDetail", Gson().toJson(error))
    return error.detail
}