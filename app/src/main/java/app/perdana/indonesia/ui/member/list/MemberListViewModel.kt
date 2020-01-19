package app.perdana.indonesia.ui.member.list

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.base.ApiResponseError
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.data.repository.MemberApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * Created by ebysofyan on 12/25/19.
 */
class MemberListViewModel : ViewModel() {
    private val repository = MemberApiRepository.getInstance()

    private var job = SupervisorJob() + Dispatchers.IO
    val progressLoading = MutableLiveData<Pair<Boolean, String>>()
    val dotsLoading = MutableLiveData<Boolean>()

    fun showDotsLoading(value: Boolean) {
        this.dotsLoading.value = value
    }

    fun showLoading(value: Pair<Boolean, String> = true to "Loading") {
        progressLoading.value = value
    }

    fun hideLoading() {
        progressLoading.value = false to ""
    }

    fun fetchMembers(token: String) = liveData(job) {
        try {
            repository?.fetchMembers(token).also {
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