package app.perdana.indonesia.data.remote.model

/**
 * Created by ebysofyan on 12/2/19.
 */


data class PresenceContainerResponse(
    val closed: Boolean? = false,
    val club: Int? = 0,
    val created: String? = null,
    val creator: Int? = 0,
    val id: Int? = 0,
    val modified: String? = null,
    val satuan: Int? = 0,
    val title: String? = null,
    val presence_items: MutableList<PresenceItem>? = mutableListOf()
)

data class PresenceItem(
    val container: Int,
    val created: String,
    val id: Int,
    val member: PresenceItemMember,
    val modified: String,
    val note: Any,
    val status: String,
    val supervisor: Any
)

data class PresenceItemMember(
    val full_name: String,
    val id: Int
)