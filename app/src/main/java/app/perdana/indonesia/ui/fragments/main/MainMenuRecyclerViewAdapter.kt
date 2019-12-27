package app.perdana.indonesia.ui.fragments.main

import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.data.remote.model.Menu
import kotlinx.android.synthetic.main.home_menu_item.view.*

/**
 * Created by ebysofyan on 12/18/19.
 */
class MainMenuRecyclerViewAdapter : BaseRecyclerViewAdapter<Menu>() {
    override fun getLayoutResourceId(): Int = R.layout.home_menu_item

    override fun onBindItem(view: View, data: Menu, position: Int) {
        view.home_menu_item_title.text = data.title
        view.home_menu_item_notification.text = data.badge
        view.home_menu_item_image.setImageResource(data.icon)
    }
}