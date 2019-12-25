package app.perdana.indonesia.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.adapters.ViewPagerAdapter
import app.perdana.indonesia.core.extension.setupActionbar
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.currentUserRole
import app.perdana.indonesia.ui.fragments.main.MainFragment
import app.perdana.indonesia.ui.fragments.presence.container.PresenceContainerFragment
import app.perdana.indonesia.ui.fragments.profile.ProfileFragment
import app.perdana.indonesia.ui.fragments.scoring.members.ScoringMemberFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.bottom_menu_home -> {
                    main_view_pager.currentItem = 0
                    true
                }
                R.id.bottom_menu_presence -> {
                    main_view_pager.currentItem = 1
                    true
                }
                R.id.bottom_menu_scoring -> {
                    if (currentUserRole != Constants.UserRole.CLUB_SATUAN_MANAGER) {
                        main_view_pager.currentItem = 1
                    } else {
                        main_view_pager.currentItem = 2
                    }
                    true
                }
                R.id.bottom_menu_profile -> {
                    main_view_pager.currentItem = main_view_pager.adapter?.count?.minus(1)!!
                    true
                }
                else -> false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        initializeUi()
    }

    private fun initializeUi() {
        main_toolbar.setupActionbar(this, "", false)
        main_bottom_navigation_view.apply {
            itemIconTintList = null
            if (currentUserRole != Constants.UserRole.CLUB_SATUAN_MANAGER) {
                menu.removeItem(R.id.bottom_menu_presence)
            }
            setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        }

        initViewPager()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private var prevMenuItem: MenuItem? = null
    private val onPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            if (prevMenuItem != null) prevMenuItem?.isChecked = false
            else main_bottom_navigation_view.menu.getItem(0).isChecked = false

            main_bottom_navigation_view.menu.getItem(position).isChecked = true
            prevMenuItem = main_bottom_navigation_view.menu.getItem(position)
        }
    }

    private fun initViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.fragments.add(MainFragment.newInstance() to getString(R.string.navigation_home_title))
        if (currentUserRole == Constants.UserRole.CLUB_SATUAN_MANAGER) {
            adapter.fragments.add(PresenceContainerFragment.newInstance() to getString(R.string.navigation_presence_title))
        }
        adapter.fragments.add(ScoringMemberFragment.newInstance() to getString(R.string.navigation_scoring_title))
        adapter.fragments.add(ProfileFragment.newInstance() to getString(R.string.navigation_profile_title))

        main_view_pager.addOnPageChangeListener(onPageChangeListener)
        main_view_pager.offscreenPageLimit = 4
        main_view_pager.adapter = adapter
    }
}
