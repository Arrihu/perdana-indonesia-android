package app.perdana.indonesia.ui.applicant.detail

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
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
import app.perdana.indonesia.ui.photo.view.PhotoViewActivity
import com.amulyakhare.textdrawable.TextDrawable
import kotlinx.android.synthetic.main.applicant_detail_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import org.jetbrains.anko.startActivity

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
        initActionListener()

        viewModel.getLoading().observe(this, Observer {
            showLoading(it)
        })

        viewModel.getProgressLoading().observe(this, Observer { state ->
            showLoading(state.first, state.second)
        })

        viewModel.setLoading(true)
    }


    private fun initActionListener() {
        applicant_detail_button_accept.setOnClickListener {
            BottomSheetDialogFragmentAccept.newInstance().also {
                it.show(supportFragmentManager, ApplicantDetailActivity::class.java.simpleName)
            }
        }
    }

    private fun onResponseHandler(response: ApiResponseModel<ArcherMemberResponse>?) {
        viewModel.setLoading(false)
//        when {
//            response?.data != null -> setArcherMemberView(response.data)
//            response?.error != null -> longToast(response.error.detail)
//            response?.exception != null -> longToast(response.exception.message.toString())
//        }
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

    @SuppressLint("SetTextI18n")
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

        val personalInfoContainer =
            findViewById<LinearLayout>(R.id.applicant_detail_personal_info_container)
        archerMemberPersonalInfo.forEach { perInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                personalInfoContainer,
                false
            )

            inflatedView.findViewById<TextView>(R.id.key_value_detail_item_title).also {
                it.text = perInfo.first
            }
            val value = inflatedView.findViewById<TextView>(R.id.key_value_detail_item_value).also {
                if (perInfo.second.endsWith(".jpg") ||
                    perInfo.second.endsWith(".png") ||
                    perInfo.second.endsWith(".JPEG")
                ) {
                    it.text = "Tekan untuk lihat photo"
                    inflatedView.setOnClickListener {
                        startActivity<PhotoViewActivity>(Constants.IMAGE_URL to perInfo.second)
                    }
                } else {
                    it.text = perInfo.second
                }
            }

            personalInfoContainer.addView(inflatedView)
        }

        archerMemberPhysicInfo.forEach { phyInfo ->
            val inflatedView = layoutInflater.inflate(
                R.layout.profile_item_view,
                applicant_detail_physic_info_item_container,
                false
            )
            inflatedView.findViewById<TextView>(R.id.key_value_detail_item_title).also {
                it.text = phyInfo.first
            }
            val value = inflatedView.findViewById<TextView>(R.id.key_value_detail_item_value).also {
                it.text = phyInfo.second
            }
            applicant_detail_physic_info_item_container.addView(inflatedView)
        }
    }

    fun onSubmitRegisterNumber(string: String) {
//        if (string.isNotEmpty()) {
//            viewModel.showProgressLoading(true to "Mengubah status peserta. Silahkan tunggu beberapa saat . . .")
//            viewModel.approveApplicantMember(
//                formattedToken,
//                archerMemberResponse?.id.toString(),
//                string
//            ).observe(this, Observer { response ->
//                approveApplicantResponseHandler(response)
//            })
//        }
    }

    private fun approveApplicantResponseHandler(response: ApiResponseModel<ArcherMemberResponse>?) {
        viewModel.hideProgressLoading()
//        when {
//            response?.data != null -> {
//                alert {
//                    title =
//                        "Penerimaan anggota berhasil. Status peserta telah diubah menjadi anggota."
//                    okButton {
//                        it.dismiss()
//
//                        val intent = Intent()
//                        setResult(Activity.RESULT_OK, intent)
//                        finish()
//                    }
//                }.show()
//            }
//            response?.error != null -> longToast(response.error.detail)
//            response?.exception != null -> longToast(response.exception.message.toString())
//        }
    }

    private var progressDialog: ProgressDialog? = null
    private fun showLoading(show: Boolean, msg: String = "Loading") {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this)
        }
        progressDialog?.apply {
            setTitle("")
            setMessage(msg)
            isIndeterminate = true
        }
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }
}
