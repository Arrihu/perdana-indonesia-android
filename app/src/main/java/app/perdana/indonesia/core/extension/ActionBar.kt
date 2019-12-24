package app.perdana.indonesia.core.extension

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Created by @ebysofyan on 6/20/19
 */

fun Toolbar.setupActionbar(
    activity: AppCompatActivity,
    title: String = "Toolbar",
    displayHomeAsUp: Boolean = true,
    navigationClickListener: (View) -> Unit = {}
) {
    activity.setSupportActionBar(this)
    activity.supportActionBar?.setDisplayHomeAsUpEnabled(displayHomeAsUp)
    activity.title = title
    this.setNavigationOnClickListener {
        navigationClickListener.invoke(it)
    }
}