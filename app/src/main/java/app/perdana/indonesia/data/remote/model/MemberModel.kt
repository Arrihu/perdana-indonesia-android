package app.perdana.indonesia.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.android.parcel.Parcelize

/**
 * Created by ebysofyan on 12/2/19.
 */

data class MemberRequest(
    val address: String? = null,
    val approved: Boolean? = false,
    val approved_by: String? = null,
    val blood_type: String? = null,
    val body_height: String? = null,
    val body_weight: String? = null,
    val born_date: String? = null,
    val born_place: String? = null,
    @Expose
    val club: String? = null,
    val created: String? = null,
    val date_register: String? = null,
    val disease_history: String? = null,
    val draw_length: String? = null,
    val full_name: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val identity_card_number: String? = null,
    val identity_card_photo: String? = null,
    val modified: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val public_photo: String? = null,
    val qrcode: String? = null,
    val religion: String? = null,
    @Expose
    val satuan: String? = null,
    val user: User
)

@Parcelize
data class ArcherMemberResponse(
    val address: String? = null,
    val approved: Boolean? = false,
    val approved_by: String? = null,
    val blood_type: String? = null,
    val body_height: String? = null,
    val body_weight: String? = null,
    val born_date: String? = null,
    val born_place: String? = null,
    @Expose
    val club: Club? = null,
    val created: String? = null,
    val date_register: String? = null,
    val disease_history: String? = null,
    val draw_length: String? = null,
    val full_name: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val identity_card_number: String? = null,
    val identity_card_photo: String? = null,
    val skck: String? = null,
    val modified: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val public_photo: String? = null,
    val qrcode: String? = null,
    val religion: String? = null,
    @Expose
    val satuan: Satuan? = null,
    val user: User
) : Parcelable

data class ClubUnitCommiteMemberResponse(
    val address: String? = null,
    val approved: Boolean? = false,
    val approved_by: String? = null,
    val blood_type: String? = null,
    val body_height: String? = null,
    val body_weight: String? = null,
    val born_date: String? = null,
    val born_place: String? = null,
    @Expose
    val club: Int? = 0,
    val created: String? = null,
    val date_register: String? = null,
    val disease_history: String? = null,
    val draw_length: String? = null,
    val full_name: String? = null,
    val gender: String? = null,
    val id: String? = null,
    val identity_card_number: String? = null,
    val identity_card_photo: String? = null,
    val modified: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    val public_photo: String? = null,
    val qrcode: String? = null,
    val religion: String? = null,
    val position: String? = null,
    val sk_number: String? = null,
    @Expose
    val satuan: Int? = null
)

@Parcelize
data class User(
    val email: String? = null,
    val first_name: String? = null,
    val last_name: String? = null,
    val username: String? = null,
    val password: String? = null
) : Parcelable
