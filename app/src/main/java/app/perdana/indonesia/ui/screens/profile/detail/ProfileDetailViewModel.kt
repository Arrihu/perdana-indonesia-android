package app.perdana.indonesia.ui.screens.profile.detail

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
class ProfileDetailViewModel : ViewModel() {
    private val memberApiRepository = MemberApiRepository.getInstance()
    private val loading = MutableLiveData<Boolean>()

    fun setLoading(show: Boolean) {
        this.loading.value = show
    }

    fun getLoading() = loading

    fun getProfile(token: String) = liveData {
        try {
            val response = memberApiRepository?.getProfile(token)
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