package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.ui.ItemOffsetDecoration
import app.perdana.indonesia.data.remote.model.PracticeScore
import app.perdana.indonesia.data.remote.model.PracticeSeries
import kotlinx.android.synthetic.main.scoring_practice_series_item_view.view.*
import org.jetbrains.anko.longToast

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
        val adapter = ScoringPracticeScoresRecyclerViewAdapter() { v, ps, adapter ->
            showScoreDialog(v, ps, adapter)
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

    private fun showScoreDialog(
        view: View,
        practiceScore: PracticeScore,
        adapter: ScoringPracticeScoresRecyclerViewAdapter
    ) {
        val inflatedView =
            LayoutInflater.from(view.context).inflate(R.layout.score_selected_view, null)
        val alertDialogBuilder = AlertDialog.Builder(view.context).apply {
            setView(inflatedView)
        }

        val scores = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val arrayAdapter =
            ArrayAdapter<Int>(view.context, android.R.layout.simple_spinner_dropdown_item, scores)
        val listView = inflatedView.findViewById<ListView>(R.id.score_selected_list_view).also {
            it.adapter = arrayAdapter
        }

        val dialog = alertDialogBuilder.create().also {
            it.show()
        }

        listView.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, position, _ ->
                view.context.longToast(scores[position].toString())

                practiceScore.score = scores[position]
                practiceScore.filled = true
                adapter.setScore(practiceScore)

                dialog.dismiss()
            }
    }
}