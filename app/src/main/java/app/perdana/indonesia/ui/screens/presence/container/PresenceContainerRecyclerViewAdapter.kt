package app.perdana.indonesia.ui.screens.presence.container

import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.fullDateTimeFormat
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.data.remote.model.PresenceContainerResponse
import kotlinx.android.synthetic.main.presence_item_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class PresenceContainerRecyclerViewAdapter(private val onItemClickListener: (PresenceContainerResponse) -> Unit) :
    BaseRecyclerViewAdapter<PresenceContainerResponse>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_item_item_view

    override fun onBindItem(view: View, data: PresenceContainerResponse, position: Int) {
        view.presence_item_image_profile.gone()
        view.presence_item_item_title.text = data.title
        view.presence_item_item_subtitle.text = data.created?.fullDateTimeFormat()
        view.presence_item_item_image_status.setImageResource(R.drawable.ic_chevron_right)

        view.presence_item_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}