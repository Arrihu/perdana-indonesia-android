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
    val archery_range: Int? = null
)

