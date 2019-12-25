package app.perdana.indonesia.ui.fragments.scoring.members

import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import kotlinx.android.synthetic.main.presence_item_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringMemberRecyclerViewAdapter(private val onItemClickListener: (ArcherMemberResponse) -> Unit) :
    BaseRecyclerViewAdapter<ArcherMemberResponse>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_item_item_view

    override fun onBindItem(view: View, data: ArcherMemberResponse, position: Int) {
        view.presence_item_item_image_status.setImageResource(R.drawable.ic_chevron_right)
        view.presence_item_image_profile.loadWithGlidePlaceholder(data.public_photo.toString())
        view.presence_item_item_title.text = data.full_name
        view.presence_item_item_subtitle.text = data.user.username

        view.presence_item_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}