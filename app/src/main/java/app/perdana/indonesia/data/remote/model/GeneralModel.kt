package app.perdana.indonesia.data.remote.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by ebysofyan on 12/23/19.
 */


data class Regional(
    val id: Int,
    val name: String
) {
    override fun toString(): String = name
}

data class Province(
    val id: Int,
    val name: String
) {
    override fun toString(): String = name
}

data class Branch(
    val id: Int,
    val name: String
) {
    override fun toString(): String = name
}

@Parcelize
data class Club(
    val id: Int,
    val name: String
) : Parcelable {
    override fun toString(): String = name
}

@Parcelize
data class Satuan(
    val id: Int,
    val name: String
) : Parcelable {
    override fun toString(): String = name
}