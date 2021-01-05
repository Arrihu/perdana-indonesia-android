package app.perdana.indonesia.ui.screens.scoring.practices.series

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.base.ApiResponseError
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.data.remote.model.PracticeContainer
import app.perdana.indonesia.data.remote.model.PracticeSeriesScore
import app.perdana.indonesia.data.repository.PracticeApiRepository
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringPracticeSeriesViewModel : ViewModel() {
    private val repository = PracticeApiRepository.getInstance()

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

    fun getPracticesContainer(token: String, containerId: String, archerId: String) =
        liveData(job) {
            try {
                repository?.getPracticesContainer(token, containerId, archerId).also { emit(it) }
            } catch (e: HttpException) {
                emit(null)
            }
        }

    fun addNewPracticesContainer(
        token: String,
        archerId: String,
        practiceContainer: PracticeContainer
    ) =
        liveData(job) {
            try {
                repository?.addNewPracticeContainer(token, archerId, practiceContainer)
                    .also { emit(it) }
            } catch (e: HttpException) {
                emit(null)
            }
        }
}