package app.perdana.indonesia.core.base

import app.perdana.indonesia.core.extension.getErrorDetail
import com.google.gson.annotations.SerializedName
import retrofit2.Response

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

data class ApiResponseError(
    val response: Response<*>?
) {
    val statusCode: Int? get() = response?.code()
    val detail: String get() = response?.errorBody()?.getErrorDetail().toString()
}

sealed class ApiResponseModel<out T> {
    data class Success<T>(val data: T) : ApiResponseModel<T>()
    data class Failure(val statusCode: Int?, val detail: String) : ApiResponseModel<Nothing>()
    data class Error(val e: Exception) : ApiResponseModel<Nothing>()
}

sealed class BaseApiResponseModel<out T> {
    data class Success<T>(val data: T) : BaseApiResponseModel<T>()
    data class Failure(val statusCode: Int?, val detail: String) :
        BaseApiResponseModel<Nothing>()

    data class Error(val e: Exception) : BaseApiResponseModel<Nothing>()
}