package app.perdana.indonesia.ui.screens.scoring.practices.add

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.ArcherMemberResponse
import app.perdana.indonesia.data.remote.model.ArcheryRange
import app.perdana.indonesia.data.remote.model.PracticeContainer
import com.google.gson.Gson
import kotlinx.android.synthetic.main.scoring_practice_container_add_activity.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import retrofit2.Response

/**
 * Created by ebysofyan on 04/01/20.
 */
class ScoringPracticeContainerAddActivity : AppCompatActivity() {
    private lateinit var viewModel: ScoringPracticeContainerAddViewModel
    private var archerMemberId : String? = null

    private val targetTypes = mutableListOf("PERDANA", "PUTA", "FITA")
    private val archeryRanges = mutableListOf<ArcheryRange>()

    private var selectedTargetType: String = ""
    private var selectedArcheryRange: ArcheryRange? = null
    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.id) {
                R.id.scoring_practice_add_spinner_target_type -> {
                    selectedTargetType = targetTypes[position].toString()
                }
                R.id.scoring_practice_add_spinner_archery_range -> {
                    selectedArcheryRange = archeryRanges[position]
                    if (selectedArcheryRange?.id == 0) {
                        scoring_practice_add_custom_name_input_layout.visible()
                    } else {
                        scoring_practice_add_custom_name_input_layout.gone()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scoring_practice_container_add_activity)
        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(ScoringPracticeContainerAddViewModel::class.java)
        archerMemberId = intent.getStringExtra(Constants.ARCHER_MEMBER_ID)
        initActionBar()
        initActionListener()
        initTargetType()

        viewModel.progressLoading.observe(this, Observer { state ->
            showLoading(state.first, state.second)
        })

        viewModel.showLoading(true to "Menyiapkan data tempat latihan")
        viewModel.fetchArcheryRanges(formattedToken).observe(this, Observer { response ->
            viewModel.hideLoading()
            initArcheryRanges(response)
        })
    }

    private fun initActionListener() {
        scoring_practice_add_button_submit.setOnClickListener {
            submit()
        }
    }

    private fun submit() {
        if(scoring_practice_add_distance_input_layout.editText?.text.toString().isEmpty()){
            scoring_practice_add_distance_input_layout.editText?.error = "Jarak harus diisi"
            return
        }

        if(scoring_practice_add_series_input_layout.editText?.text.toString().isEmpty()){
            scoring_practice_add_series_input_layout.editText?.error = "Jumlah Rambahan harus diisi"
            return
        }

        if(scoring_practice_add_arrow_input_layout.editText?.text.toString().isEmpty()){
            scoring_practice_add_arrow_input_layout.editText?.error = "Jumlah Arrow harus diisi"
            return
        }

        if (selectedTargetType.isEmpty()){
            longToast("Pilih jenis target terlebih dahulu")
            return
        }

        if (selectedArcheryRange?.id == 0){
            if (scoring_practice_add_custom_name_input_layout.editText?.text.toString().isEmpty()){
                scoring_practice_add_custom_name_input_layout.editText?.error = "Tentukan nama lokasi latihan"
                return
            }
        }

        val practiceContainer = PracticeContainer(
            address = scoring_practice_add_custom_name_input_layout.editText?.text.toString(),
            distance = scoring_practice_add_distance_input_layout.editText?.text.toString(),
            series = scoring_practice_add_series_input_layout.editText?.text.toString(),
            arrow = scoring_practice_add_arrow_input_layout.editText?.text.toString(),
            note = scoring_practice_add_notes_input_layout.editText?.text.toString(),
            target_type = selectedTargetType
        )
        if(selectedArcheryRange?.id != 0){
            practiceContainer.archery_range = selectedArcheryRange?.id
        }

        viewModel.showLoading(true to "Membuat form skoring . . .")
        viewModel.addNewPracticesContainer(formattedToken, archerMemberId.toString(), practiceContainer).observe(this, Observer {response ->
            onAddNewPracticesContainerResponse(response)
        })
    }

    private fun onAddNewPracticesContainerResponse(response: Response<PracticeContainer>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    longToast("Form skoring berhasil di simpan")
                    val intent = Intent()
                    intent.putExtra(Constants.HAS_CHANGE, true)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
                else -> {
                    Log.e("RESPONSE", Gson().toJson(response.raw().toString()))
                    longToast(response.errorBody()?.getErrorDetail().toString())
                }
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun initActionBar() {
        scoring_practice_add_toolbar.setupActionbar(
            this,
            "Buat Form Skoring Baru",
            true
        ) {
            finish()
        }
    }

    private fun initTargetType() {
        val adapter = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            targetTypes
        )
        scoring_practice_add_spinner_target_type.adapter = adapter
        scoring_practice_add_spinner_target_type.onItemSelectedListener = onItemSelectedListener
    }

    private fun initArcheryRanges(response: Response<List<ArcheryRange>>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    this.archeryRanges.clear()
                    this.archeryRanges.addAll(response.body() ?: mutableListOf<ArcheryRange>())
                    this.archeryRanges.add(ArcheryRange(name = "Lainnya", id = 0))

                    val adapter = ArrayAdapter<ArcheryRange>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        this.archeryRanges
                    )
                    scoring_practice_add_spinner_archery_range.adapter = adapter
                    scoring_practice_add_spinner_archery_range.onItemSelectedListener =
                        onItemSelectedListener
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
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