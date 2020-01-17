package app.perdana.indonesia.core.extension

import android.annotation.SuppressLint
import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by ebysofyan on 12/25/19.
 */

@SuppressLint("SimpleDateFormat")
fun String.fullDateFormat(format: String = "EEEE, dd MMMM yyyy"): String {
    val validDate = this.replace("T", " ")
    val date = SimpleDateFormat("yyyy-MM-dd").parse(validDate)
    return SimpleDateFormat(format, Locale("in", "ID")).format(date)
}

@SuppressLint("SimpleDateFormat")
fun Date.fullDateFormat(format: String = "EEEE, dd MMMM yyyy"): String {
    return SimpleDateFormat(format, Locale("in", "ID")).format(this)
}

@SuppressLint("SimpleDateFormat")
fun String.fullDateTimeFormat(format: String = "EEEE, dd MMMM yyyy hh:mm:ss"): String {
    val validDate = this.replace("T", " ")
    val date = SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(validDate)
    return SimpleDateFormat(format, Locale("in", "ID")).format(date)
}

fun String.toClass(context: Context?): Class<*>? = Class.forName(context?.packageName + this)