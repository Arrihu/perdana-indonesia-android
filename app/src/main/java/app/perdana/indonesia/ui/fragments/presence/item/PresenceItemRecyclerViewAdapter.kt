package app.perdana.indonesia.ui.fragments.presence.item

import android.view.View
import app.perdana.indonesia.BuildConfig
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.data.remote.model.PresenceItem
import kotlinx.android.synthetic.main.presence_item_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class PresenceItemRecyclerViewAdapter(private val onItemClickListener: (PresenceItem) -> Unit) :
    BaseRecyclerViewAdapter<PresenceItem>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_item_item_view

    override fun onBindItem(view: View, data: PresenceItem, position: Int) {
        view.presence_item_image_profile.loadWithGlidePlaceholder(BuildConfig.BASE_URL + data.member.public_photo)
        view.presence_item_item_title.text = data.member.full_name
        view.presence_item_item_subtitle.text = data.member.member_id

        when (data.status) {
            "0" -> view.presence_item_item_image_status.setImageResource(R.drawable.ic_close_red)
            "1" -> view.presence_item_item_image_status.setImageResource(R.drawable.ic_check_green)
            "2" -> view.presence_item_item_image_status.setImageResource(R.drawable.ic_note_orange)
        }

        view.presence_item_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}