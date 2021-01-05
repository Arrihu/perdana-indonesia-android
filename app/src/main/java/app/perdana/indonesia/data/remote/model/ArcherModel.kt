package app.perdana.indonesia.data.remote.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by ebysofyan on 12/2/19.
 */

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val token: String,
    val user: LoginResponseUser
)

data class LoginResponseUser(
    val full_name: String,
    val group: String,
    val username: String
)

data class Member(
    val address: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    @SerializedName("qrcode")
    val qrCode: String? = null
)

data class Archer(
    val address: String,
    val approved: Boolean,
    val approved_by: Int,
    val blood_type: String,
    val body_height: String,
    val body_weight: String,
    val born_date: String,
    val born_place: String,
    val club: Club,
    val created: String,
    val date_register: String,
    val disease_history: String,
    val draw_length: String,
    val full_name: String,
    val gender: String,
    val id: Int,
    val identity_card_number: String,
    val identity_card_photo: String,
    val is_active: Boolean,
    val kelurahan: Int,
    val modified: String,
    val phone: String,
    val photo: String,
    val public_photo: String,
    val qrcode: String,
    val region_code_name: String,
    val religion: String,
    val skck: String,
    val username: String,
    val verified: Boolean
)

@Parcelize
data class Club(
    val address: String,
    val central: Int,
    val city_code: String,
    val created: String,
    val date_register: String,
    val district_code: String,
    val id: Int,
    val logo: String,
    val modified: String,
    val name: String,
    val org_type: String,
    val organisation_id: Int,
    val province_code: String,
    val village: Int
) : Parcelable