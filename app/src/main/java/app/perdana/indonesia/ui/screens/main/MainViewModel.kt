package app.perdana.indonesia.ui.screens.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.data.repository.GeneralApiRepository
import app.perdana.indonesia.data.repository.MemberApiRepository

class MainViewModel : ViewModel() {
    private val generalApiRepository = GeneralApiRepository.getInstance()
    private val loading = MutableLiveData<Boolean>()

    fun showLoading(show: Boolean) {
        this.loading.value = show
    }

    fun getLoading() = loading

    fun fetchDashboardData(token: String) = liveData {
        try {
            val response = generalApiRepository?.fetchDashboardData(token)
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
