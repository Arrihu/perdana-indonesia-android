package app.perdana.indonesia.ui.scanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.data.repository.MemberApiRepository
import retrofit2.HttpException

/**
 * Created by ebysofyan on 12/24/19.
 */
class CheckMembershipViewModel : ViewModel() {
    private val memberApiRepository = MemberApiRepository.getInstance()
    private val loading = MutableLiveData<Boolean>()

    fun showLoading(show: Boolean) {
        this.loading.value = show
    }

    fun getLoading() = loading

    fun checkMembership(headerMap: HashMap<String, String>, archerId: String) = liveData {
        try {
            val response = memberApiRepository?.checkMembership(headerMap, archerId)
            when (response?.isSuccessful) {
                true -> emit(ApiResponseModel.Success(response.body()))
                else -> emit(
                    ApiResponseModel.Failure(
                        response?.code(),
                        response?.errorBody()?.getErrorDetail().toString()
                    )
                )
            }
        } catch (e: Exception) {
            emit(ApiResponseModel.Error(e))
        }

    }
}