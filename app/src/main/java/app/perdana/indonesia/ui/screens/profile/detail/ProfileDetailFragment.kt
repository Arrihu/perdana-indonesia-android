package app.perdana.indonesia.ui.screens.profile.detail

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.BuildConfig
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.Archer
import app.perdana.indonesia.data.remote.model.ClubUnitCommiteMemberResponse
import app.perdana.indonesia.ui.login.LoginActivity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_item_view.*
import org.w3c.dom.Text


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
        profile_button_logout.setOnClickListener(this)

        viewModel.getLoading().observe(this.viewLifecycleOwner, Observer {
            showLoading(it)
        })

        viewModel.setLoading(true)
        viewModel.getProfile(context?.formattedToken.toString())
            .observe(this.viewLifecycleOwner, Observer { response ->
                handleGetProfile(response)
            })
    }

    private fun handleGetProfile(response: ApiResponseModel<Archer?>) {
        viewModel.setLoading(false)
        when (response) {
            is ApiResponseModel.Success -> {
                onGetProfileSuccess(response.data)
            }
            is ApiResponseModel.Failure -> {
                Toasty.error(requireContext(), response.detail).show()
            }
            is ApiResponseModel.Error -> Toasty.error(
                requireContext(),
                response.e.message.toString()
            ).show()
        }

    }


    override fun onClick(v: View?) {
        when (v) {
            profile_button_logout -> {
                LocalStorage.clear(requireContext())
                startActivity(Intent(requireContext(), LoginActivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        if (show) profile_loading.visible()
        else profile_loading.gone()
    }

    private fun onGetProfileSuccess(archer: Archer?) {
        setArcherMemberView(archer)
        viewModel.setLoading(false)
    }

    private fun setArcherMemberView(archer: Archer?) {
        profile_text_name.text = archer?.full_name ?: "-"
        profile_image_profile.loadWithGlidePlaceholder(BuildConfig.BASE_URL + archer?.public_photo.toString())

        val archerMemberPersonalInfo = mutableListOf(
            "No. Anggota" to (archer?.username ?: "-"),
            "Nama Lengkap" to (archer?.full_name ?: "-"),
            "No. KTP / NIK" to (archer?.identity_card_number ?: "-"),
            "Asal Klub" to (archer?.club?.name ?: "-"),
            "No. HP" to (archer?.phone ?: "-"),
            "Gender" to (archer?.gender ?: "-"),
            "Tempat, Tanggal Lahir" to "${archer?.born_place
                ?: "-"}, ${archer?.born_date ?: "-"}",
            "Alamat" to (archer?.address ?: "-"),
            "Golongan Darah" to (archer?.blood_type ?: "-"),
            "Riwayat Penyakit" to (archer?.disease_history ?: "-"),
            "Tinggi Badan" to (archer?.body_height ?: "-"),
            "Berat Badan" to (archer?.body_weight ?: "-"),
            "Panjang Tarikan" to (archer?.draw_length ?: "-")
        )


        archerMemberPersonalInfo.forEach { perInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                profile_personal_info_container,
                false
            )
            inflatedView.apply {
                findViewById<TextView>(R.id.key_value_detail_item_title).text = perInfo.first
                findViewById<TextView>(R.id.key_value_detail_item_value).text = perInfo.second
            }
            profile_personal_info_container.addView(inflatedView)
        }

        profile_button_qrcode.setOnClickListener {
            val dialogView: View = layoutInflater.inflate(R.layout.qrcode_dialog_view, null)
            val dialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
            dialog.setView(dialogView)
            dialogView.findViewById<ImageView>(R.id.qrcode_image_view)
                .loadWithGlidePlaceholder(BuildConfig.BASE_URL + archer?.qrcode)
            dialogView.findViewById<View>(R.id.qrcode_text_close).setOnClickListener {
                dialog.dismiss()
            }

            dialog.show()
        }
    }

    private fun setClubUnitManagerView(commiteMemberResponse: ClubUnitCommiteMemberResponse) {
        profile_text_name.text = commiteMemberResponse.full_name ?: "-"
        profile_image_profile.loadWithGlidePlaceholder(BuildConfig.BASE_URL + commiteMemberResponse.public_photo.toString())

        val personalInfo = mutableListOf(
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
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                profile_personal_info_container,
                false
            )
            inflatedView.apply {
                key_value_detail_item_title.text = perInfo.first
                key_value_detail_item_value.text = perInfo.second
            }
            profile_personal_info_container.addView(inflatedView)
        }
    }
}
