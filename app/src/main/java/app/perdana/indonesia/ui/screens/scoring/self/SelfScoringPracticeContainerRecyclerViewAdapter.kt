package app.perdana.indonesia.ui.screens.scoring.self

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.fullDateTimeFormat
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.data.remote.model.PracticeContainer
import kotlinx.android.synthetic.main.presence_item_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class SelfScoringPracticeContainerRecyclerViewAdapter(private val onItemClickListener: (PracticeContainer) -> Unit) :
    BaseRecyclerViewAdapter<PracticeContainer>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_item_item_view

    @SuppressLint("SetTextI18n")
    override fun onBindItem(view: View, data: PracticeContainer, position: Int) {
        view.presence_item_image_profile.gone()
        view.presence_item_item_title.text = "Skoring, ${data.arrow} Arrow ${data.series} Rambahan pada jarak ${data.distance} Meter"
        view.presence_item_item_subtitle.apply {
            text = if (data.completed){
                setTextColor(ContextCompat.getColor(view.context, R.color.color_red))
                "Skoring telah selesai"
            }else{
                setTextColor(ContextCompat.getColor(view.context, R.color.color_green))
                "Skoring sedang berjalan"
            }
        }
        view.presence_item_item_image_status.setImageResource(R.drawable.ic_chevron_right)

        view.presence_item_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}