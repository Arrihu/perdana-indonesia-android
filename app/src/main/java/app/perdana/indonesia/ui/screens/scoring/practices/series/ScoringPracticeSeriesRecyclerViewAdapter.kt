package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.ui.ItemOffsetDecoration
import app.perdana.indonesia.data.remote.model.PracticeSeries
import kotlinx.android.synthetic.main.scoring_practice_series_item_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeSeriesRecyclerViewAdapter(private val onItemClickListener: (PracticeSeries) -> Unit) :
    BaseRecyclerViewAdapter<PracticeSeries>() {
    override fun getLayoutResourceId(): Int = R.layout.scoring_practice_series_item_view

    @SuppressLint("SetTextI18n")
    override fun onBindItem(view: View, data: PracticeSeries, position: Int) {
        view.practice_series_item_series_title.text = "RAMBAHAN ${data.serie}"
        initScoreRecyclerView(view, data)
    }

    private fun initScoreRecyclerView(view: View, data: PracticeSeries) {
        val adapter = ScoringPracticeScoresRecyclerViewAdapter() {
            onItemClickListener.invoke(data)
        }
        view.practice_series_item_score_recycler_view.layoutManager =
            GridLayoutManager(view.context, 6)
        view.practice_series_item_score_recycler_view.addItemDecoration(
            ItemOffsetDecoration(
                view.context,
                R.dimen.item_offset
            )
        )
        view.practice_series_item_score_recycler_view.adapter = adapter
        adapter.addItems(data.scores.toMutableList())
    }
}