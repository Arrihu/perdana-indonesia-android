package app.perdana.indonesia.ui.screens.main

import android.view.View
import androidx.core.content.ContextCompat
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.data.remote.model.DashboardData
import app.perdana.indonesia.data.remote.model.Menu
import kotlinx.android.synthetic.main.dashboard_item_view.view.*
import kotlinx.android.synthetic.main.home_menu_item.view.*
import java.util.*

/**
 * Created by ebysofyan on 12/18/19.
 */
class DashboardRecyclerViewAdapter : BaseRecyclerViewAdapter<DashboardData>() {
    override fun getLayoutResourceId(): Int = R.layout.dashboard_item_view

    override fun onBindItem(view: View, data: DashboardData, position: Int) {
        val randomColor = Constants.dashboardBackgroundColors.shuffled().first()
        view.dashboard_item_view_container.setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                randomColor
            )
        )
        view.dashboard_item_view_text_title.text = data.title
        view.dashboard_item_view_text_value.text = data.value.toString()
    }
}