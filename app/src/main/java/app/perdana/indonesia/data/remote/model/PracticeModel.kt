package app.perdana.indonesia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ebysofyan on 12/18/19.
 */

class TopScoring(
    val number: String? = null,
    val name: String? = null,
    val score: String? = null,
    val distance: String? = null,
    val target: String? = null
)

@Parcelize
data class PracticeContainer(
    val address: String? = null,
    val arrow: String,
    val completed: Boolean = false,
    val created: String? = null,
    val distance: String,
    val id: Int? = null,
    val member: Int? = null,
    val modified: String? = null,
    val note: String? = null,
    val series: String,
    val total: Int = 0,
    val target_type: String,
    var archery_range: Int? = null
) : Parcelable

@Parcelize

data class PracticeContainerSeries(
    val address: String? = null,
    val arrow: String,
    val completed: Boolean = false,
    val created: String? = null,
    val distance: String,
    val id: Int? = null,
    val member: Int? = null,
    val modified: String? = null,
    val note: String? = null,
    val series: String,
    val target_type: String,
    var archery_range: Int? = null,
    val practice_series: List<PracticeSeries> = mutableListOf()
) : Parcelable

@Parcelize
data class PracticeSeries(
    var closed: Boolean,
    val created: String,
    val id: Int,
    val modified: String,
    val photo: String,
    val practice_container: Int,
    var scores: List<PracticeScore> = mutableListOf(),
    var serie: Int,
    var total: Int = 0
) : Parcelable


@Parcelize
data class PracticeScore(
    val id: Int,
    var score: Int,
    val serie: Int,
    var filled: Boolean
) : Parcelable

data class PracticeSeriesScore(
    val scores: MutableList<PracticeScore>
)