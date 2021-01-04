package app.perdana.indonesia.ui.screens.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.toClass
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.ui.ItemOffsetDecoration
import app.perdana.indonesia.core.utils.Constants
import app.perdana.indonesia.core.utils.currentUserRole
import app.perdana.indonesia.data.remote.model.Menu
import com.synnapps.carouselview.CarouselView
import kotlinx.android.synthetic.main.main_fragment.*
import org.jetbrains.anko.longToast

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private val mainMenus = mutableListOf(
            Menu(
                "Pendaftar",
                R.drawable.ic_account_circle_outline_white_48dp,
                "",
                ".ui.applicant.list.ApplicantActivity"
            ),
            Menu(
                "Anggota",
                R.drawable.ic_account_circle_outline_white_48dp,
                "",
                ".ui.member.list.MemberListActivity"
            ),
            Menu("Archery Range", R.drawable.ic_map_marker_radius_white_48dp, "", ""),
            Menu("Organisasi", R.drawable.ic_account_group_outline_white_48dp, "", ""),
            Menu("Pengaturan", R.drawable.ic_settings_outline_white_48dp, "", "")
        )

        val sliders = mutableListOf(
            "https://i0.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/WhatsApp-Image-2020-08-04-at-19.19-2-scaled.jpg?resize=1080%2C945&ssl=1",
            "https://i2.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/Mask-Group-5.jpg?fit=1440%2C1120&ssl=1",
            "https://i2.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/Mask-Group-3.jpg?fit=1440%2C1120&ssl=1"
        )
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: TopScoreRecyclerViewAdapter
    private lateinit var mainMenuAdapter: MainMenuRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also {
            initSlider(it)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {
//        initializeTopScoresRecyclerView()
    }

    private fun initSlider(view: View) {
        val carouselView = view.findViewById<CarouselView>(R.id.main_carousel_view)
        carouselView.pageCount = sliders.size
        carouselView.setImageListener { position, imageView ->
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.loadWithGlidePlaceholder(sliders[position])
        }
    }

//    private fun initializeTopScoresRecyclerView() {
//        adapter = TopScoreRecyclerViewAdapter()
//        main_fragment_score_recycler_view.layoutManager = LinearLayoutManager(context)
//        main_fragment_score_recycler_view.adapter = adapter
//
//        adapter.addItems(topScorers)
//    }

}
