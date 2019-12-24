package app.perdana.indonesia.ui.fragments.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import app.perdana.indonesia.data.repository.MemberApiRepository
import retrofit2.HttpException

/**
 * Created by ebysofyan on 12/24/19.
 */
class ProfileViewModel : ViewModel() {
    private val repository = MemberApiRepository.getInstance()
    private val loading = MutableLiveData<Boolean>()

    fun setLoading(show: Boolean) {
        this.loading.value = show
    }

    fun getLoading() = loading

    fun getProfile(token: String) = liveData {
        try {
            repository?.getProfile(token).also { emit(it) }
        } catch (e: HttpException) {
            emit(null)
        }
    }
}