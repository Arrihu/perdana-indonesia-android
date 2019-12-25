package app.perdana.indonesia.ui.fragments.presence.item

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.data.repository.PresenceApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException

/**
 * Created by ebysofyan on 12/25/19.
 */
class PresenceItemViewModel : ViewModel() {
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

    fun getPresencesContainer(token: String, id: String) = liveData(job) {
        try {
            repository?.getPresencesContainer(token, id).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun changeItemStatus(token: String, id: String, queryMap: HashMap<String, String>) =
        liveData(job) {
            try {
                repository?.changeItemStatus(token, id, queryMap).also { emit(it) }
            } catch (e: HttpException) {
                emit(null)
            }
        }
}