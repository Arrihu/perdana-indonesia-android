package app.perdana.indonesia.core.extension

import android.content.Context
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File

fun Context.isFormInputRequiredValid(
    forms: MutableList<Pair<TextInputLayout, String>>,
    action: () -> Unit
) {
    val result = mutableListOf<Pair<TextInputLayout, String>>()
    for (form in forms) {
        if (form.first.editText?.text?.trim()?.length == 0) {
            form.first.editText?.error = form.second
            result.add(form)
        }
    }

    if (result.size == 0) {
        action.invoke()
    }
}

fun HashMap<String, RequestBody>.addMapRequestBody(
    field: String,
    file: File
): HashMap<String, RequestBody> {
    this[field] = RequestBody.create(MediaType.parse("multipart/form-data"), file)
    return this
}

fun HashMap<String, RequestBody>.addMapRequestBody(
    field: String,
    value: String,
    mediaType: String = "multipart/form-data"
): HashMap<String, RequestBody> {
    this[field] = RequestBody.create(MediaType.parse(mediaType), value)
    return this
}

fun File.addToRequestBody(): RequestBody? =
    RequestBody.create(MediaType.parse("multipart/form-data"), this)
