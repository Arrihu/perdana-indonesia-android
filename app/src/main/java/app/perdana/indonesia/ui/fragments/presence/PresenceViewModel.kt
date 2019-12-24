package app.perdana.indonesia.ui.fragments.presence

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.data.repository.PresenceApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException

class PresenceViewModel : ViewModel() {
    private val repository = PresenceApiRepository.getInstance()

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

    fun fetchPresencesContainer(token: String) = liveData(job) {
        try {
            repository?.fetchPresencesContainer(token).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun getPresencesContainer(token: String, id: String) = liveData(job) {
        try {
            repository?.getPresencesContainer(token, id).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun getPresencesContainer(token: String, id: String, status: String) = liveData(job) {
        try {
            repository?.changeItemStatus(token, id, status).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun addNewPresenceContainer(token: String, title: String) = liveData(job) {
        try {
            repository?.addNewPresenceContainer(token, title).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }
}
