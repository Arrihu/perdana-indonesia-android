package app.perdana.indonesia.ui.screens.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import app.perdana.indonesia.R
import app.perdana.indonesia.core.base.ApiResponseModel
import app.perdana.indonesia.core.extension.gone
import app.perdana.indonesia.core.extension.loadWithGlidePlaceholder
import app.perdana.indonesia.core.extension.visible
import app.perdana.indonesia.core.utils.formattedToken
import app.perdana.indonesia.data.remote.model.DashboardContainer
import app.perdana.indonesia.data.remote.model.Menu
import com.synnapps.carouselview.CarouselView
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        val sliders = mutableListOf(
            "https://i0.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/WhatsApp-Image-2020-08-04-at-19.19-2-scaled.jpg?resize=1080%2C945&ssl=1",
            "https://i2.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/Mask-Group-5.jpg?fit=1440%2C1120&ssl=1",
            "https://i2.wp.com/perdanaindonesia.or.id/wp-content/uploads/2020/12/Mask-Group-3.jpg?fit=1440%2C1120&ssl=1"
        )
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: DashboardRecyclerViewAdapter
    private lateinit var dashboardAdapter: DashboardRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false).also {
            initSlider(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        initializeUi()
    }

    private fun initializeUi() {
        initializeDashboardRecyclerView()
        viewModel.getLoading().observe(viewLifecycleOwner, Observer { loadingState ->
            handleGetLoading(loadingState)
        })

        fetchDashboardData()
        main_swipe_refresh_layout.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                main_swipe_refresh_layout.isRefreshing = false
                fetchDashboardData()
            }, 250L)
        }
    }

    private fun fetchDashboardData() {
        viewModel.showLoading(true)
        viewModel.fetchDashboardData(requireContext().formattedToken)
            .observe(viewLifecycleOwner, Observer { response ->
                handleFetchDashboardData(response)
            })

    }

    private fun handleFetchDashboardData(response: ApiResponseModel<DashboardContainer?>) {
        viewModel.showLoading(false)
        when (response) {
            is ApiResponseModel.Success -> {
                adapter.addItems(response.data?.data?.toMutableList() ?: mutableListOf())
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

    private fun handleGetLoading(loadingState: Boolean) {
        if (loadingState) {
            main_loading.visible()
        } else {
            main_loading.gone()
        }
    }

    private fun initSlider(view: View) {
        val carouselView = view.findViewById<CarouselView>(R.id.main_carousel_view)
        carouselView.pageCount = sliders.size
        carouselView.setImageListener { position, imageView ->
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            imageView.loadWithGlidePlaceholder(sliders[position])
        }
    }

    private fun initializeDashboardRecyclerView() {
        adapter = DashboardRecyclerViewAdapter()
        main_recycler_view.layoutManager = GridLayoutManager(context, 2)
        main_recycler_view.adapter = adapter
    }

}
