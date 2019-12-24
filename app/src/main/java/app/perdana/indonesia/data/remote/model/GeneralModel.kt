package app.perdana.indonesia.data.remote.model

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

data class Club(
    val id: Int,
    val name: String
) {
    override fun toString(): String = name
}

data class Satuan(
    val id: Int,
    val name: String
) {
    override fun toString(): String = name
}