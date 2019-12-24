package app.perdana.indonesia.ui.register

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.getErrorDetail
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.utils.ProgressDialogHelper
import app.perdana.indonesia.data.remote.model.*
import com.google.gson.JsonElement
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.button_primary.*
import kotlinx.android.synthetic.main.register_activity.*
import kotlinx.android.synthetic.main.toolbar_light_theme.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import pl.aprilapps.easyphotopicker.DefaultCallback
import pl.aprilapps.easyphotopicker.EasyImage
import retrofit2.Response
import java.io.File

/**
 * Created by ebysofyan on 11/26/19.
 */
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val ID_CARD_PHOTO_REQUEST_CODE = 101
    }

    private var selectedIdCardFile: File? = null
    private lateinit var viewModel: RegisterViewModel

    private val regionals = mutableListOf<Regional>()
    private val provinces = mutableListOf<Province>()
    private val branchs = mutableListOf<Branch>()
    private val clubs = mutableListOf<Club>()
    private val units = mutableListOf<Satuan>()

    private var branchId = 0
    private val onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            when (parent?.id) {
                R.id.register_club_spinner_regional -> {
                    if (regionals[position].id != 0) fetchProvinces(regionals[position].id.toString())
                }
                R.id.register_club_spinner_province -> {
                    if (provinces[position].id != 0) fetchBranchs(provinces[position].id.toString())
                    Log.e("BRANCH", provinces[position].id.toString())
                }
                R.id.register_club_spinner_branch -> {
                    branchId = branchs[position].id

                    if (branchId != 0) {
                        if (register_radio_club.isChecked) fetchClubs(branchId.toString())
                        else fetchSatuans(branchId.toString())
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        initializeUi()
    }

    private fun initializeUi() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)

        initActionBar()
        initActionListener()
        checkPermission()
    }

    private fun checkPermission() {
        Dexter.withActivity(this).withPermissions(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                initApiRequest()
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }
        }).check()
    }

    private fun initApiRequest() {
        viewModel.getLoading().observe(this, Observer { loading ->
            showLoading(loading.first, loading.second)
        })

        viewModel.showLoading(true to "Menyiapkan data Regional")
        viewModel.fetchRegionals().observe(this, Observer { response ->
            onRegionalsLoadedResponse(response)
        })
    }

    private fun onRegionalsLoadedResponse(response: Response<List<Regional>>?) {
        viewModel.hideLoading()

        if (response != null) {
            when (response.isSuccessful) {
                true -> onRegionalsResponseSuccess(response.body())
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun onRegionalsResponseSuccess(regionalsResponse: List<Regional>?) {
        regionalsResponse?.let {
            this.regionals.clear()
            this.regionals.add(Regional(0, "Pilih Regional"))
            this.regionals.addAll(it)
            val adapter = ArrayAdapter<Regional>(
                this@RegisterActivity,
                android.R.layout.simple_spinner_dropdown_item,
                regionals
            )
            register_club_spinner_regional.adapter = adapter
            register_club_spinner_regional.onItemSelectedListener = onItemSelectedListener
        }
    }

    private fun fetchProvinces(regionalId: String) {
        viewModel.showLoading(true to "Menyiapkan data Provinsi")
        viewModel.fetchProvinces(regionalId).observe(this, Observer { response ->
            onProvincesLoadedResponse(response)
        })
    }

    private fun onProvincesLoadedResponse(response: Response<List<Province>>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> onProvincesResponseSuccess(response.body())
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun onProvincesResponseSuccess(provincesResponse: List<Province>?) {
        provincesResponse?.let {
            this.provinces.clear()
            this.provinces.add(Province(0, "Pilih Provinsi"))
            this.provinces.addAll(it)
            val adapter = ArrayAdapter<Province>(
                this@RegisterActivity,
                android.R.layout.simple_spinner_dropdown_item,
                provinces
            )
            register_club_spinner_province.adapter = adapter
            register_club_spinner_province.onItemSelectedListener = onItemSelectedListener
        }
    }

    private fun fetchBranchs(provinceId: String) {
        viewModel.showLoading(true to "Menyiapkan data Cabang")
        viewModel.fetchBranchs(provinceId).observe(this, Observer { response ->
            onBranchsLoadedResponse(response)
        })
    }

    private fun onBranchsLoadedResponse(response: Response<List<Branch>>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> onBranchsResponseSuccess(response.body())
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun onBranchsResponseSuccess(branchsResponse: List<Branch>?) {
        branchsResponse?.let {
            this.branchs.clear()
            this.branchs.add(Branch(0, "Pilih Cabang"))
            this.branchs.addAll(it)
            val adapter = ArrayAdapter<Branch>(
                this@RegisterActivity,
                android.R.layout.simple_spinner_dropdown_item,
                branchs
            )
            register_club_spinner_branch.adapter = adapter
            register_club_spinner_branch.onItemSelectedListener = onItemSelectedListener
        }
    }

    private fun fetchClubs(branchId: String) {
        viewModel.showLoading(true to "Menyiapkan data Klub")
        viewModel.fetchClubs(branchId).observe(this, Observer { response ->
            onClubsLoadedResponse(response)
        })
    }

    private fun onClubsLoadedResponse(response: Response<List<Club>>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    this.clubs.clear()
                    this.clubs.addAll(response.body() ?: mutableListOf<Club>())
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun fetchSatuans(branchId: String) {
        viewModel.showLoading(true to "Menyiapkan data Satuan")
        viewModel.fetchUnits(branchId).observe(this, Observer { response ->
            onSatuansLoadedResponse(response)
        })
    }

    private fun onSatuansLoadedResponse(response: Response<List<Satuan>>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    this.units.clear()
                    this.units.addAll(response.body() ?: mutableListOf<Satuan>())
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun initActionBar() {
        _toolbar.setupActionbar(this, getString(R.string.register), true) {
            finish()
        }
        primary_button_dark.text = getString(R.string.register)
    }

    private fun initActionListener() {
        primary_button_dark.setOnClickListener(this)
        register_card_photo_image_camera.setOnClickListener(this)
        register_club_input_layout.setOnClickListener(this)

        register_radio_group_org.setOnCheckedChangeListener { group, checkedId ->
            val radio = group.findViewById<RadioButton>(checkedId)
            register_club_input_layout.hint = "Pilih ${radio.text} (Tekan untuk memilih)"

            when (checkedId) {
                R.id.register_radio_club -> {
                    fetchClubs(branchId.toString())
                    resetClubUnit()
                }
                R.id.register_radio_unit -> {
                    fetchSatuans(branchId.toString())
                    resetClubUnit()
                }
            }
        }
    }

    private fun resetClubUnit() {
        club = null
        unit = null
        register_club_input_layout.text?.clear()
    }

    private fun validateRegisterForm(): Boolean {
        if (register_username_input_layout.editText?.text.toString().isEmpty()) {
            register_username_input_layout.editText?.error = "Username tidak boleh kosong"
            return false
        }

        if (register_password_input_layout.editText?.text.toString().isEmpty()) {
            register_password_input_layout.editText?.error = "Password tidak boleh kosong"
            return false
        }

        if (register_radio_club.isChecked) {
            if (club == null) {
                longToast("Pilih club terlebih dahulu")
                return false
            }
        } else {
            if (unit == null) {
                longToast("Pilih satuan terlebih dahulu")
                return false
            }
        }


        if (register_full_name_input_layout.editText?.text.toString().isEmpty()) {
            register_full_name_input_layout.editText?.error = "Nama tidak boleh kosong"
            return false
        }

        if (register_phone_input_layout.editText?.text.toString().isEmpty()) {
            register_phone_input_layout.editText?.error = "No. HP tidak boleh kosong"
            return false
        }

        if (register_address_input_layout.editText?.text.toString().isEmpty()) {
            register_address_input_layout.editText?.error = "Alamat tidak boleh kosong"
            return false
        }

        if (register_id_card_input_layout.editText?.text.toString().isEmpty()) {
            register_id_card_input_layout.editText?.error = "No. KTP / NIK tidak boleh kosong"
            return false
        }

        if (selectedIdCardFile == null) {
            longToast("Lengkapi photo / scan KTP anda")
            return false
        }

        return true
    }

    private fun submit() {
        if (validateRegisterForm()) {
            val member = MemberRequest(
                user = User(
                    username = register_username_input_layout.editText?.text.toString(),
                    password = register_password_input_layout.editText?.text.toString()
                ),
                club = club?.id.toString(),
                satuan = unit?.id.toString(),
                gender = if (register_radio_man.isChecked) "pria" else "wanita",
                full_name = register_full_name_input_layout.editText?.text.toString(),
                phone = register_phone_input_layout.editText?.text.toString(),
                address = register_address_input_layout.editText?.text.toString(),
                identity_card_number = register_id_card_input_layout.editText?.text.toString()
            )

            val photos = mutableListOf(selectedIdCardFile)
            viewModel.showLoading(true to "Sedang mengirim data ke server, silahkan menunggu . . .")
            viewModel.register(member, photos).observe(this, Observer { response ->
                onRegisterResponse(response)
            })
        }
    }

    private fun onRegisterResponse(response: Response<JsonElement>?) {
        viewModel.hideLoading()
        if (response != null) {
            when (response.isSuccessful) {
                true -> {
                    alert {
                        message =
                            "Register berhasil, akun anda akan di verifikasi oleh pengurus klub sebelum bisa digunakan"
                        isCancelable = false
                        okButton {
                            clearForm()
                            it.dismiss()
                        }
                    }.show()
                }
                else -> longToast(response.errorBody()?.getErrorDetail().toString())
            }
        } else longToast(getString(R.string.no_internet_connection))
    }

    private fun clearForm() {
        resetClubUnit()
        selectedIdCardFile = null
        register_card_photo_image.setImageResource(0)
        register_username_input_layout.editText?.text?.clear()
        register_password_input_layout.editText?.text?.clear()
        register_full_name_input_layout.editText?.text?.clear()
        register_address_input_layout.editText?.text?.clear()
        register_id_card_input_layout.editText?.text?.clear()
        register_id_card_input_layout.editText?.text?.clear()
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {
                submit()
            }

            v === register_card_photo_image_camera -> {
                EasyImage.openChooserWithGallery(this, "Pilih Photo", ID_CARD_PHOTO_REQUEST_CODE)
            }

            v === register_club_input_layout -> {
                showClubSatuanDialog()
            }
        }
    }

    private var club: Club? = null
    private var unit: Satuan? = null
    private fun showClubSatuanDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.club_units_dialog_view, null)
        dialogBuilder.setView(view)

        val textHeader = view.findViewById<TextView>(R.id.club_unit_dialog_header)
        val imageClose = view.findViewById<ImageView>(R.id.club_unit_dialog_close)
        val listView = view.findViewById<ListView>(R.id.club_unit_dialog_list_view)
        val adapter = if (register_radio_club.isChecked) {
            ArrayAdapter<Club>(view.context, android.R.layout.simple_list_item_1, clubs)
        } else {
            ArrayAdapter<Satuan>(view.context, android.R.layout.simple_list_item_1, units)
        }
        listView.adapter = adapter

        val dialog = dialogBuilder.create().also { it.show() }
        textHeader.text = if (register_radio_club.isChecked) "Pilih Klub" else "Pilih Satuan"
        imageClose.setOnClickListener { dialog.dismiss() }
        listView.setOnItemClickListener { _, _, position, _ ->
            if (register_radio_club.isChecked) {
                club = clubs[position]
                register_club_input_layout.setText(club?.name.toString())
            } else {
                unit = units[position]
                register_club_input_layout.setText(unit?.name.toString())
            }

            dialog.dismiss()
        }
    }

    private fun showLoading(show: Boolean, msg: String = "Loading") {
        val progressDialog = ProgressDialogHelper.getInstance(this)
        progressDialog?.setMessage(msg)
        if (show) progressDialog?.show()
        else progressDialog?.dismiss()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : DefaultCallback() {
                override fun onImagesPicked(
                    p0: MutableList<File>,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {
                    selectedIdCardFile = p0[0]
                    register_card_photo_image.loadWithGlidePlaceholder(selectedIdCardFile)
                }
            })
    }
}