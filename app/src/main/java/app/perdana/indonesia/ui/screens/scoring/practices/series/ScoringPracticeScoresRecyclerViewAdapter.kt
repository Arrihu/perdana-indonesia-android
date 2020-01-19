package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.annotation.SuppressLint
import android.view.View
import androidx.core.content.ContextCompat
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.BaseRecyclerViewAdapter
import app.perdana.indonesia.data.remote.model.PracticeScore
import kotlinx.android.synthetic.main.circle_view.view.*

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeScoresRecyclerViewAdapter(private val onItemClickListener: (View, PracticeScore, ScoringPracticeScoresRecyclerViewAdapter) -> Unit) :
    BaseRecyclerViewAdapter<PracticeScore>() {
    override fun getLayoutResourceId(): Int = R.layout.circle_view

    fun setScore(practiceScore: PracticeScore) {
        getItems().firstOrNull { it.id == practiceScore.id }?.apply {
            score = practiceScore.score
            filled = practiceScore.filled
        }
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindItem(view: View, data: PracticeScore, position: Int) {
        if (data.filled) {
            view.circle_view_container.background =
                ContextCompat.getDrawable(view.context, R.drawable.bg_circle_solid_primary)
            view.circle_view_text.setTextColor(
                ContextCompat.getColor(
                    view.context,
                    android.R.color.white
                )
            )
        }

        view.circle_view_text.text = data.score.toString()
        if (!data.filled) {
            view.circle_view_container.setOnClickListener {
                onItemClickListener.invoke(view, data, this)
            }
        }
    }
}