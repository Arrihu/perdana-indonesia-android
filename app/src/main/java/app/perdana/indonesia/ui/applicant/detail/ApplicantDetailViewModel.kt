package app.perdana.indonesia.ui.applicant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.base.ApiResponseError
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.data.repository.MemberApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Created by ebysofyan on 12/24/19.
 */
class ApplicantDetailViewModel : ViewModel() {
    private val repository = MemberApiRepository.getInstance()

    private var job = SupervisorJob() + Dispatchers.IO

    private val loading = MutableLiveData<Boolean>()

    fun setLoading(show: Boolean) {
        this.loading.value = show
    }

    fun getLoading() = loading

    fun getMemberApplicant(token: String, id  :String) = liveData(job) {
        try {
            repository?.getMemberApplicant(token, id).also {
                when (it?.isSuccessful) {
                    true -> emit(ApiResponseModel(data = it.body()))
                    else -> {
                        ApiResponseError(it).also { error ->
                            emit(ApiResponseModel(error = error))
                        }
                    }
                }
            }
        } catch (e: Exception) {
            emit(ApiResponseModel(exception = e))
        }
    }
}