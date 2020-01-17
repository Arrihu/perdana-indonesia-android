package app.perdana.indonesia.ui.applicant.detail

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import com.amulyakhare.textdrawable.TextDrawable
import kotlinx.android.synthetic.main.applicant_detail_activity.*
import kotlinx.android.synthetic.main.profile_item_view.*
import org.jetbrains.anko.longToast

class ApplicantDetailActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var viewModel: ApplicantDetailViewModel
    private var archerMemberResponse: ArcherMemberResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.applicant_detail_activity)

        archerMemberResponse = intent.getParcelableExtra(Constants.ARCHER_MEMBER_RESPONSE_OBJ)
        viewModel = ViewModelProvider(this).get(ApplicantDetailViewModel::class.java)
        initializeUi()
    }

    private fun initializeUi() {
        initActionBar()

        viewModel.getLoading().observe(this, Observer {
            showLoading(it)
        })

        viewModel.setLoading(true)
        viewModel.getMemberApplicant(formattedToken.toString(), archerMemberResponse?.id.toString())
            .observe(this, Observer { response ->
                onResponseHandler(response)
            })
    }

    private fun onResponseHandler(response: ApiResponseModel<ArcherMemberResponse>?) {
        viewModel.setLoading(false)
        when {
            response?.data != null -> setArcherMemberView(response.data)
            response?.error != null -> longToast(response.error.detail)
            response?.exception != null -> longToast(response.exception.message.toString())
        }
    }

    private fun initActionBar() {
        applicant_detail_toolbar.setupActionbar(
            this,
            "Detail Anggota",
            true
        ) {
            finish()
        }
    }

    override fun onClick(v: View?) {

    }

    private fun showLoading(show: Boolean) {
        if (show) applicant_detail_loading.visible()
        else applicant_detail_loading.gone()
    }

    private fun setArcherMemberView(archerMemberResponse: ArcherMemberResponse) {
        applicant_detail_text_name.text = archerMemberResponse.full_name ?: "-"

        val text = archerMemberResponse.full_name?.take(0)
        val colorPrimary = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        val colorWhite = ContextCompat.getColor(this, android.R.color.white)
        val textDrawable = TextDrawable.builder().beginConfig()
            .textColor(colorPrimary)
            .endConfig().buildRound(text, colorWhite)
        applicant_detail_image_applicant_profile.loadWithGlidePlaceholder(textDrawable)

        val archerMemberPersonalInfo = mutableListOf<Pair<String, String>>(
            "Username" to (archerMemberResponse.user.username ?: "-"),
            "No. HP" to (archerMemberResponse.phone ?: "-"),
            "Gender" to (archerMemberResponse.gender ?: "-"),
            "Tempat, Tanggal Lahir" to "${archerMemberResponse.born_place
                ?: "-"}, ${archerMemberResponse.born_date ?: "-"}",
            "Alamat" to (archerMemberResponse.address ?: "-"),
            "Golongan Darah" to (archerMemberResponse.blood_type ?: "-"),
            "Riwayat Penyakit" to (archerMemberResponse.disease_history ?: "-"),
            "No KTP / NIK" to (archerMemberResponse.identity_card_number ?: "-"),
            "Photo / Scan KTP" to (archerMemberResponse.identity_card_photo ?: "-"),
            "Photo / Scan SKCK" to (archerMemberResponse.skck ?: "-")
        )

        val archerMemberPhysicInfo = mutableListOf(
            "Tinggi Badan" to (archerMemberResponse.body_height ?: "-"),
            "Berat Badan" to (archerMemberResponse.body_weight ?: "-"),
            "Panjang Tarikan" to (archerMemberResponse.draw_length ?: "-")
        )

        archerMemberPersonalInfo.forEach { perInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                applicant_detail_personal_info_container,
                false
            )
            inflatedView.apply {
                key_value_detail_item_title.text = perInfo.first
                key_value_detail_item_value.text = perInfo.second
            }
            applicant_detail_personal_info_container.addView(inflatedView)
        }

        archerMemberPhysicInfo.forEach { phyInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                applicant_detail_physic_info_item_container,
                false
            )
            inflatedView.apply {
                key_value_detail_item_title.text = phyInfo.first
                key_value_detail_item_value.text = phyInfo.second
            }
            applicant_detail_physic_info_item_container.addView(inflatedView)
        }
    }
}
