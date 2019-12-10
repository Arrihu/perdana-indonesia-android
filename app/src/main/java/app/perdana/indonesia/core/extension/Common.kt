package app.perdana.indonesia.core.extension

fun Boolean.then(action: () -> Unit) {
    if (this) action.invoke()
}

fun Boolean.ifNot(action: () -> Unit) {
    if (!this) action.invoke()
}