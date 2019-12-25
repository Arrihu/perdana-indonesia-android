package app.perdana.indonesia.ui.fragments.scoring.practices

import android.annotation.SuppressLint
import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.fullDateTimeFormat
import app.perdana.indonesia.data.remote.model.PracticeContainer
import kotlinx.android.synthetic.main.presence_container_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeContainerRecyclerViewAdapter(private val onItemClickListener: (PracticeContainer) -> Unit) :
    BaseRecyclerViewAdapter<PracticeContainer>() {
    override fun getLayoutResourceId(): Int = R.layout.presence_container_item_view

    @SuppressLint("SetTextI18n")
    override fun onBindItem(view: View, data: PracticeContainer, position: Int) {
        view.presence_container_item_title.text = "Latihan ${data.created.fullDateTimeFormat()}"
        view.presence_container_item_date.text = data.address

        view.presence_container_item_container.setOnClickListener {
            onItemClickListener.invoke(data)
        }
    }
}