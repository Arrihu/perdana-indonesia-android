package app.perdana.indonesia.data.remote.model

import com.google.gson.annotations.SerializedName

/**
 * Created by ebysofyan on 12/2/19.
 */

data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val member: Member,
    val role: String? = null,
    val token: String? = null
)

data class Member(
    val address: String? = null,
    val gender: String? = null,
    val phone: String? = null,
    val photo: String? = null,
    @SerializedName("qrcode")
    val qrCode: String? = null
)