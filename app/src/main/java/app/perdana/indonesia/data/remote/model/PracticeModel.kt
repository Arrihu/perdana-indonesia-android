package app.perdana.indonesia.data.remote.model

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

data class PracticeContainer(
    val address: String,
    val arrow: Int,
    val completed: Boolean,
    val created: String,
    val distance: Double,
    val id: Int,
    val member: Int,
    val modified: String,
    val note: String,
    val series: Int,
    val target_type: String
)

