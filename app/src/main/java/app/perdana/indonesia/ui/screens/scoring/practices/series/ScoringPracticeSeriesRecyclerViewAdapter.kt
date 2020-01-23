package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.ui.ItemOffsetDecoration
import app.perdana.indonesia.data.remote.model.PracticeScore
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
        view.practice_series_item_series_total_score.text = "Total : ${data.total}"

        if (data.closed) view.practice_series_item_series_send.gone()
        view.practice_series_item_series_send.setOnClickListener {
            onItemClickListener.invoke(data)
        }

        initScoreRecyclerView(view, data)
    }

    private fun countTotal() {
        getItems().forEach { serie ->
            serie.total = 0
            serie.scores.forEach { score ->
                serie.total += score.score
            }
        }
        notifyDataSetChanged()
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
        val dialog = alertDialogBuilder.create().also {
            it.show()
        }

        val scores = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val arrayAdapter = ScoringSelectedItemArrayAdapter(view.context, scores) {
            practiceScore.score = it ?: 0
            practiceScore.filled = true
            adapter.setScore(practiceScore)

            countTotal()
            dialog.dismiss()
        }
        val listView = inflatedView.findViewById<ListView>(R.id.score_selected_list_view).also {
            it.adapter = arrayAdapter
        }
    }

    fun updateDataPracticeSerie(practiceSeries: PracticeSeries) {
        val ps = getItems().firstOrNull { it.id == practiceSeries.id }
        ps?.total = practiceSeries.total
        ps?.closed = practiceSeries.closed
        ps?.scores = practiceSeries.scores

        notifyDataSetChanged()
    }
}

class ScoringSelectedItemArrayAdapter(
    context: Context,
    private val items: List<Int>,
    private val onClickListener: (Int?) -> Unit
) :
    ArrayAdapter<Int>(context, 0, items) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val score = getItem(position)
        var newConvertedView = convertView
        if (newConvertedView == null) {
            newConvertedView =
                LayoutInflater.from(context).inflate(R.layout.circle_view, parent, false)
        }

        newConvertedView?.findViewById<TextView>(R.id.circle_view_text).also {
            it?.text = score.toString()
        }
        newConvertedView?.findViewById<LinearLayout>(R.id.circle_view_container).also {
            it?.setOnClickListener {
                onClickListener.invoke(score)
            }
        }
        return newConvertedView!!
    }
}