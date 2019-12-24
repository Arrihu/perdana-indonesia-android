package app.perdana.indonesia.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.core.extension.then
import app.perdana.indonesia.data.remote.model.MemberRequest
import app.perdana.indonesia.data.repository.GeneralApiRepository
import app.perdana.indonesia.data.repository.UserApiRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import retrofit2.HttpException
import java.io.File

/**
 * Created by ebysofyan on 12/23/19.
 */
class RegisterViewModel : ViewModel() {
    private val repository: GeneralApiRepository? = GeneralApiRepository.getInstance()
    private val userRepository: UserApiRepository? = UserApiRepository.getInstance()
    private var job = SupervisorJob() + Dispatchers.IO
    private val loading = MutableLiveData<Pair<Boolean, String>>()

    fun showLoading(value: Pair<Boolean, String> = true to "Loading") {
        loading.value = value
    }

    fun hideLoading() {
        loading.value = false to ""
    }

    fun getLoading() = loading

    fun fetchRegionals() = liveData(job) {
        try {
            repository?.fetchRegionals().also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun fetchProvinces(regionalId: String) = liveData(job) {
        try {
            repository?.fetchProvinces(regionalId).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun fetchBranchs(province: String) = liveData(job) {
        try {
            repository?.fetchBranchs(province).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun fetchClubs(branch: String) = liveData(job) {
        try {
            repository?.fetchClubs(branch).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun fetchUnits(branch: String) = liveData(job) {
        try {
            repository?.fetchUnits(branch).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun register(memberRequest: MemberRequest, photos: MutableList<File?>) = liveData(job) {
        try {
            userRepository?.register(memberRequest, photos).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }

    fun cancelJob() = job.isActive.then { job.cancel() }
}