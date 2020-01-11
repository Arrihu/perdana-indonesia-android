package app.perdana.indonesia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ebysofyan on 12/2/19.
 */

data class PresenceContainerRequest(
    var title: String? = null,
    var latitude: String? = null,
    var longitude: String? = null
)

@Parcelize
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
) : Parcelable

@Parcelize
data class PresenceItem(
    val container: Int,
    val created: String,
    val id: Int,
    val user: PresenceItemMember,
    val modified: String,
    val note: String? = null,
    var status: String,
    val supervisor: PresenceItemMember
) : Parcelable

@Parcelize
data class PresenceItemMember(
    val member_id: String,
    val full_name: String,
    val photo: String,
    val public_photo: String,
    val id: Int
) : Parcelable