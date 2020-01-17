package app.perdana.indonesia.ui.applicant.list

import android.view.View
import androidx.core.content.ContextCompat
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import com.amulyakhare.textdrawable.TextDrawable
import kotlinx.android.synthetic.main.presence_item_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ApplicantRecyclerViewAdapter(private val onItemClickListener: (ArcherMemberResponse) -> Unit) :
    BaseRecyclerViewAdapter<ArcherMemberResponse>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_item_item_view

    override fun onBindItem(view: View, data: ArcherMemberResponse, position: Int) {
        val text = data.full_name?.take(0)
        val color = ContextCompat.getColor(view.context, R.color.colorPrimaryDark)
        val textDrawable = TextDrawable.builder().beginConfig()
            .width(100).height(100)
            .endConfig().buildRound(text, color)

        view.presence_item_image_profile.loadWithGlidePlaceholder(textDrawable)
        view.presence_item_item_title.text = data.full_name
        view.presence_item_item_subtitle.text = data.identity_card_number
        view.presence_item_item_image_status.setImageResource(R.drawable.ic_chevron_right)

        view.presence_item_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}