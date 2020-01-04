package app.perdana.indonesia.data.remote.model

/**
 * Created by ebysofyan on 05/01/20.
 */

data class ArcheryRange(
    val address: String? = null,
    val club: String? = null,
    val id: Int,
    val latitude: String? = null,
    val longitude: String? = null,
    val name: String? = null,
    val unit: String? = null
){
    override fun toString(): String = this.name.toString()
}