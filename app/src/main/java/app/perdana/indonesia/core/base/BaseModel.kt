package app.perdana.indonesia.core.base

import com.google.gson.annotations.SerializedName

/**
 * Created by ebysofyan on 12/3/19.
 */

data class PaginatedLink(
    val next: String? = null,
    val prev: String? = null
)

data class BasePaginatedResponse<T>(
    val links: MutableList<PaginatedLink>,
    val count: Int,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("page_number")
    val pageNumber: Int,
    val results: MutableList<T>
)

data class PerdanaError(
    val detail: String
)