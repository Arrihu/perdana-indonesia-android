package app.perdana.indonesia.ui.screens.main

import android.view.View
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.data.remote.model.TopScoring
import kotlinx.android.synthetic.main.main_score_item_view.view.*

/**
 * Created by ebysofyan on 12/18/19.
 */
class TopScoreRecyclerViewAdapter : BaseRecyclerViewAdapter<TopScoring>() {
    override fun getLayoutResourceId(): Int = R.layout.main_score_item_view

    override fun onBindItem(view: View, data: TopScoring, position: Int) {
        view.main_score_item_text_number.text = data.number
        view.main_score_item_text_name.text = data.name
        view.main_score_item_text_score.text = data.score
        view.main_score_item_text_distance.text = data.distance
        view.main_score_item_text_target.text = data.target
    }
}