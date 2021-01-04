package app.perdana.indonesia.ui.register.webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.os.Build
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.compress
import app.perdana.indonesia.core.extension.setupActionbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.activity_register_webview.*
import kotlinx.android.synthetic.main.toolbar_dark_theme.*
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception

class RegisterWebViewActivity : AppCompatActivity() {
    private val webView: WebView by lazy { findViewById<WebView>(R.id.register_webview_web_view) }

    companion object {
        const val REGISTER_URL =
            "https://member.perdanaindonesia.or.id/app/perdana/indonesia/archer/registration"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_webview)
        _toolbar_dark.setupActionbar(this, "Pendaftaran Anggota", true) {
            finish()
        }
        setupWebView()
    }

    private fun openCameraWithPermission() {
        Dexter.withActivity(this).withPermissions(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                EasyImage.openChooserWithGallery(this@RegisterWebViewActivity, "Choose", 9999)
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: MutableList<PermissionRequest>?,
                token: PermissionToken?
            ) {

            }
        }).check()
    }

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                this@RegisterWebViewActivity.filePathCallback = filePathCallback
                openCameraWithPermission()
                return true
            }

            override fun onProgressChanged(view: WebView, progress: Int) {
                register_webview_progress.progress = progress
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                register_webview_progress.visibility = View.VISIBLE
                register_webview_progress.progress = 0
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                register_webview_progress.progress = 100
                register_webview_progress.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                handler?.proceed()
            }

            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url?.toString() ?: "")
                return super.shouldOverrideUrlLoading(view, request)
            }

        }
        webView.loadUrl(REGISTER_URL)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        EasyImage.handleActivityResult(
            requestCode,
            resultCode,
            data,
            this,
            object : EasyImage.Callbacks {
                override fun onImagePickerError(
                    p0: Exception?,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {

                }

                override fun onImagesPicked(
                    p0: MutableList<File>,
                    p1: EasyImage.ImageSource?,
                    p2: Int
                ) {
                    this@RegisterWebViewActivity.filePathCallback?.onReceiveValue(
                        arrayOf(
                            Uri.fromFile(
                                p0[0].compress(this@RegisterWebViewActivity)
                            )
                        )
                    )
                }

                override fun onCanceled(p0: EasyImage.ImageSource?, p1: Int) {
                }
            })
    }
}