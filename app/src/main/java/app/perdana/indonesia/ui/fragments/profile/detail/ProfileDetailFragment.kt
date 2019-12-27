package app.perdana.indonesia.ui.fragments.profile.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.BuildConfig
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.core.utils.currentUserRole
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import app.perdana.indonesia.data.remote.model.ClubUnitCommiteMemberResponse
import app.perdana.indonesia.ui.intro.auth.AuthIntroActivity
import com.google.gson.Gson
import com.google.gson.JsonElement
import kotlinx.android.synthetic.main.button_outlined_primary.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_item_view.view.*
import org.jetbrains.anko.longToast
import retrofit2.Response

class ProfileDetailFragment : Fragment(), View.OnClickListener {

    companion object {
        fun newInstance() =
            ProfileDetailFragment()
    }

    private lateinit var viewModel: ProfileDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProfileDetailViewModel::class.java)

        initializeUi()
    }

    private fun initializeUi() {
        primary_button_outlined_dark.text = getString(R.string.logout)
        primary_button_outlined_dark.setOnClickListener(this)

        viewModel.getLoading().observe(this.viewLifecycleOwner, Observer {
            showLoading(it)
        })

        viewModel.setLoading(true)
        viewModel.getProfile(context?.formattedToken.toString())
            .observe(this.viewLifecycleOwner, Observer { response ->
                onProfileResponse(response)
            })
    }

    private fun onProfileResponse(response: Response<JsonElement>?) {
        if (response != null) {
            when (response.isSuccessful) {
                true -> onGetProfileSuccess(response.body())
                else -> context?.longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else context?.longToast(getString(R.string.no_internet_connection))
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_outlined_dark -> {
                LocalStorage.clear(v.context)
                startActivity(Intent(v.context, AuthIntroActivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) profile_loading.visible()
        else profile_loading.gone()
    }

    private fun onGetProfileSuccess(jsonElement: JsonElement?) {
        when (context?.currentUserRole) {
            Constants.UserRole.ARCHER -> {
                val archerMemberResponse = Gson().fromJson<ArcherMemberResponse>(
                    jsonElement,
                    ArcherMemberResponse::class.java
                )
                profile_physic_info_container.visible()
                setArcherMemberView(archerMemberResponse)
            }
            Constants.UserRole.CLUB_SATUAN_MANAGER -> {
                val commiteMemberResponse = Gson().fromJson<ClubUnitCommiteMemberResponse>(
                    jsonElement,
                    ClubUnitCommiteMemberResponse::class.java
                )
                profile_physic_info_container.gone()
                setClubUnitManagerView(commiteMemberResponse)
            }
            else -> {

            }
        }

        viewModel.setLoading(false)
    }

    private fun setArcherMemberView(archerMemberResponse: ArcherMemberResponse) {
        profile_text_name.text = archerMemberResponse.full_name ?: "-"
        profile_image_profile.loadWithGlidePlaceholder(BuildConfig.BASE_URL + archerMemberResponse.public_photo.toString())

        val archerMemberPersonalInfo = mutableListOf<Pair<String, String>>(
            "Username" to (archerMemberResponse.user.username ?: "-"),
            "No. HP" to (archerMemberResponse.phone ?: "-"),
            "Gender" to (archerMemberResponse.gender ?: "-"),
            "Tempat, Tanggal Lahir" to "${archerMemberResponse.born_place
                ?: "-"}, ${archerMemberResponse.born_date ?: "-"}",
            "Alamat" to (archerMemberResponse.address ?: "-"),
            "No KTP / NIK" to (archerMemberResponse.identity_card_number ?: "-"),
            "Golongan Darah" to (archerMemberResponse.blood_type ?: "-"),
            "Riwayat Penyakit" to (archerMemberResponse.disease_history ?: "-")
        )

        val archerMemberPhysicInfo = mutableListOf(
            "Tinggi Badan" to (archerMemberResponse.body_height ?: "-"),
            "Berat Badan" to (archerMemberResponse.body_weight ?: "-"),
            "Panjang Tarikan" to (archerMemberResponse.draw_length ?: "-")
        )

        archerMemberPersonalInfo.forEach { perInfo ->
            val inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.profile_item_view, profile_personal_info_container, false)
            inflatedView.apply {
                key_value_detail_item_title.text = perInfo.first
                key_value_detail_item_value.text = perInfo.second
            }
            profile_personal_info_container.addView(inflatedView)
        }

        archerMemberPhysicInfo.forEach { phyInfo ->
            val inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.profile_item_view, profile_physic_info_item_container, false)
            inflatedView.apply {
                key_value_detail_item_title.text = phyInfo.first
                key_value_detail_item_value.text = phyInfo.second
            }
            profile_physic_info_item_container.addView(inflatedView)
        }
    }

    private fun setClubUnitManagerView(commiteMemberResponse: ClubUnitCommiteMemberResponse) {
        profile_text_name.text = commiteMemberResponse.full_name ?: "-"
        profile_image_profile.loadWithGlidePlaceholder(BuildConfig.BASE_URL + commiteMemberResponse.public_photo.toString())

        val personalInfo = mutableListOf<Pair<String, String>>(
            "No. HP" to (commiteMemberResponse.phone ?: "-"),
            "Gender" to (commiteMemberResponse.gender ?: "-"),
            "Tempat, Tanggal Lahir" to "${commiteMemberResponse.born_place
                ?: "-"}, ${commiteMemberResponse.born_date ?: "-"}",
            "Alamat" to (commiteMemberResponse.address ?: "-"),
            "No KTP / NIK" to (commiteMemberResponse.identity_card_number ?: "-"),
            "Golongan Darah" to (commiteMemberResponse.blood_type ?: "-"),
            "Riwayat Penyakit" to (commiteMemberResponse.disease_history ?: "-"),
            "Jabatan" to (commiteMemberResponse.position ?: "-"),
            "No. SK" to (commiteMemberResponse.sk_number ?: "-")
        )

        personalInfo.forEach { perInfo ->
            val inflatedView = LayoutInflater.from(context)
                .inflate(R.layout.profile_item_view, profile_personal_info_container, false)
            inflatedView.apply {
                key_value_detail_item_title.text = perInfo.first
                key_value_detail_item_value.text = perInfo.second
            }
            profile_personal_info_container.addView(inflatedView)
        }
    }
}
