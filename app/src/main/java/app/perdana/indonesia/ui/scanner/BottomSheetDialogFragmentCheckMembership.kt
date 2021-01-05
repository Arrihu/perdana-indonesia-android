package app.perdana.indonesia.ui.scanner

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.dialog.RoundedBottomSheetDialogFragment
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.Archer
import app.perdana.indonesia.ui.applicant.detail.ApplicantDetailActivity
import app.perdana.indonesia.ui.screens.profile.detail.ProfileDetailViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.bottom_sheet_archer_information.*
import kotlinx.android.synthetic.main.bottom_sheet_input_register_number.*
import kotlinx.android.synthetic.main.profile_fragment.*
import kotlinx.android.synthetic.main.profile_item_view.*

/**
 * Created by ebysofyan on 17/01/20.
 */
class BottomSheetDialogFragmentCheckMembership(private val archerId: String) :
    RoundedBottomSheetDialogFragment() {
    companion object {
        fun newInstance(archerId: String) = BottomSheetDialogFragmentCheckMembership(archerId)
    }

    private lateinit var viewModel: CheckMembershipViewModel
    private var isAuthenticated: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_archer_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckMembershipViewModel::class.java)
        isAuthenticated =
            LocalStorage.getString(requireContext(), Constants.TOKEN)?.isNotEmpty() ?: false
        initializeUi()
    }

    private fun initializeUi() {
        viewModel.getLoading().observe(this.viewLifecycleOwner, Observer {
            handleGetLoading(it)
        })

        viewModel.showLoading(true)
        val headerMap = hashMapOf<String, String>()
        if (isAuthenticated == true) {
            headerMap["Authorization"] = requireContext().formattedToken
        }
        viewModel.checkMembership(headerMap, archerId)
            .observe(this.viewLifecycleOwner, Observer { response ->
                handleCheckMembership(response)
            })
    }

    private fun handleGetLoading(show: Boolean) {
        if (show) bottom_sheet_archer_loading.visible()
        else bottom_sheet_archer_loading.gone()
    }

    private fun handleCheckMembership(response: ApiResponseModel<Archer?>) {
        viewModel.showLoading(false)
        when (response) {
            is ApiResponseModel.Success -> {
                onCheckMembershipSuccess(response.data)
            }
            is ApiResponseModel.Failure -> {
                Toasty.error(requireContext(), response.detail).show()
                this.dismiss()
            }
            is ApiResponseModel.Error -> Toasty.error(
                requireContext(),
                response.e.message.toString()
            ).show()
        }

    }

    private fun onCheckMembershipSuccess(archer: Archer?) {
        val archerMemberPersonalInfo = if (isAuthenticated == true) {
            mutableListOf(
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
        } else {
            mutableListOf(
                "No. Anggota" to (archer?.username ?: "-"),
                "Nama Lengkap" to (archer?.full_name ?: "-"),
                "Asal Klub" to (archer?.club?.name ?: "-")
            )
        }

        archerMemberPersonalInfo.forEach { perInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                bottom_sheet_archer_info_container,
                false
            )
            inflatedView.apply {
                findViewById<TextView>(R.id.key_value_detail_item_title).text = perInfo.first
                findViewById<TextView>(R.id.key_value_detail_item_value).text = perInfo.second
            }
            bottom_sheet_archer_info_container.addView(inflatedView)
        }

    }
}