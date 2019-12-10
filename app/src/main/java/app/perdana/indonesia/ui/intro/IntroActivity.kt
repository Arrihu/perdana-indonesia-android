package app.perdana.indonesia.ui.intro

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.perdana.indonesia.R
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.LocalStorage
import app.perdana.indonesia.ui.main.MainActivity
import kotlinx.android.synthetic.main.button_primary.*
import kotlinx.android.synthetic.main.button_unpriority.*
import kotlinx.android.synthetic.main.intro_activity.*
import kotlinx.android.synthetic.main.textview_primary_heading.*

/**
 * Created by ebysofyan on 12/2/19.
 */
class IntroActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var imagesAdapter: IntroImagesViewPagerAdapter
    private val introImages = mutableListOf<String>(
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu7MJdDUdk-MsXIrLHFN_EgQNTgewJ0BuFUcAARqsgNTfbPNyt&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu7MJdDUdk-MsXIrLHFN_EgQNTgewJ0BuFUcAARqsgNTfbPNyt&s",
        "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSu7MJdDUdk-MsXIrLHFN_EgQNTgewJ0BuFUcAARqsgNTfbPNyt&s"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.intro_activity)

        initializeUi()
    }

    private fun initializeUi() {
        primary_heading_text.apply {
            text = getString(R.string.app_name)
//            gravity = Gravity.CENTER
        }

        intro_text_description_body.text = getString(R.string.lorem)
        primary_button_dark.text = getString(R.string.next)
        transparent_unpriority_button.text = getString(R.string.skip)

        initViewPager()
        initActionListener()
    }

    private fun initViewPager() {
        imagesAdapter = IntroImagesViewPagerAdapter()
        imagesAdapter.setValues(introImages)
        intro_view_pager.adapter = imagesAdapter
        intro_dots_view.setViewPager(intro_view_pager)
    }

    private fun initActionListener() {
        primary_button_dark.setOnClickListener(this)
        transparent_unpriority_button.setOnClickListener(this)
    }

    private fun onViewPagerNext() {
        if (introImages.size != (intro_view_pager.currentItem + 1)) {
            intro_view_pager.setCurrentItem(intro_view_pager.currentItem + 1, true)
        } else {
            onViewPagerSkip()
        }
    }

    private fun onViewPagerSkip() {
        LocalStorage.put(this, Constants.IS_INTRO_DISPLAYED, "1")
        finish()

        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onClick(v: View?) {
        when {
            v === primary_button_dark -> {
                onViewPagerNext()
            }
            v == transparent_unpriority_button -> {
                onViewPagerSkip()
            }
        }
    }
}