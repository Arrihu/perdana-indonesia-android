package app.perdana.indonesia.ui.fragments.presence

import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.fullDateTimeFormat
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import kotlinx.android.synthetic.main.presence_container_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class PresenceContainerRecyclerVieAdapter(private val onItemClickListener: (PresenceContainerResponse) -> Unit) :
    BaseRecyclerViewAdapter<PresenceContainerResponse>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_container_item_view

    override fun onBindItem(view: View, data: PresenceContainerResponse, position: Int) {
        view.presence_container_item_title.text = data.title
        view.presence_container_item_date.text = data.created?.fullDateTimeFormat()

        view.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}