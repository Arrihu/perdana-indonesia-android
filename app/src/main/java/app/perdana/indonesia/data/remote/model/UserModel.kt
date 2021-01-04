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