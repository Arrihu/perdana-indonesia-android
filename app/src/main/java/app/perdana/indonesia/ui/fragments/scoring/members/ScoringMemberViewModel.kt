package app.perdana.indonesia.ui.fragments.scoring.members

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.data.repository.MemberApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.HttpException

/**
 * Created by ebysofyan on 12/25/19.
 */
class ScoringMemberViewModel : ViewModel() {
    private val memberApiRepository = MemberApiRepository.getInstance()

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
            memberApiRepository?.fetchMembers(token).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }
}