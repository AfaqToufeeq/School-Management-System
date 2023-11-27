package com.app.admin.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.R
import com.app.admin.databinding.FragmentDashboardBinding
import com.app.admin.adapters.DashboardAdapter
import com.app.admin.adapters.NewsAdapter
import com.app.admin.callbacks.OnItemClick
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.AdminRepository
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.viewmodel.AdminViewModel
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.AdminViewModelFactory
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class DashboardFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var adminViewModel: AdminViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private var  newsAdapter = NewsAdapter()
    private val timer = Timer()
    private lateinit var viewModel: RetrofitViewModel
    private var loaderShown = false
    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        fetchRepoData()
        setRecyclerView()
        events()
    }

    private fun init() {
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }

        val adminRepository = AdminRepository()
        adminViewModel = ViewModelProvider(this, AdminViewModelFactory(adminRepository))[AdminViewModel::class.java]

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun fetchRepoData() {
        fetchBadges()
        fetchNews()
    }

    private fun fetchNews() {
        viewModel.getNewsEvents(USER_TYPE, PickerManager.token!! )
    }

    private fun fetchBadges() {
        viewModel.fetchBatches(USER_TYPE, PickerManager.token!! )
    }

    private fun events() {
       /* binding.profileCL.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment,
                Bundle().apply { putString(MAIN_MENU, "Profile View")})
        }*/

        binding.logOut.setOnClickListener { logout() }
    }

    private fun setObservers() {
        dashboardViewModelObserver()
        newEventsObserver()
    }

    private fun newEventsObserver() {
        viewModel.getNews.observe(viewLifecycleOwner) { news->
            if (!news.isNullOrEmpty()) {
                newsAdapter.submitList(news)
                callHandler()
            }
        }
    }

    private fun dashboardViewModelObserver() {
        adminViewModel.apply {
            dashboardItemsLiveData.observe(viewLifecycleOwner) { dashboardAdapter.submitList(it) }
        }
    }

    private fun setTimerToScroll(layoutManager: LinearLayoutManager) {
        // Schedule automatic scrolling every 5 seconds
        timer.scheduleAtFixedRate(5 * 1000, 5 * 1000) {
            requireActivity().runOnUiThread {
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition = if (currentPosition < layoutManager.itemCount - 1) currentPosition + 1 else 0
                binding.viewPagerDashboard.smoothScrollToPosition(nextPosition)
            }
        }
    }

    private fun setRecyclerView() {
        binding.apply {
            dashboardRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

            setTimerToScroll(
                LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL,
                    false
                ).also { viewPagerDashboard.layoutManager = it }
            )

            dashboardAdapter = DashboardAdapter(this@DashboardFragment)
            viewPagerDashboard.adapter = newsAdapter
            dashboardRecyclerView.adapter = dashboardAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    override fun clickListener(position: Int, value: String) {
        openFragment(value)
    }

    private fun openFragment(title: String) {
        val bundle = Bundle().apply {
            putString(MAIN_MENU, title)
        }

        findNavController().apply {
            when (title) {
                "Add Teachers" -> navigate(R.id.action_dashboardFragment_to_addTeacherFragment, bundle)
                "Add Students" -> navigate(R.id.action_dashboardFragment_to_addStudentFragment, bundle)
                "Add Finance" -> navigate(R.id.action_dashboardFragment_to_addFinanceFragment, bundle)
                "Add Events" -> navigate(R.id.action_dashboardFragment_to_addNewsFragment, bundle)
                "View Teachers" -> navigate(R.id.action_dashboardFragment_to_viewTeachersFragment, bundle)
                "View Students" -> navigate(R.id.action_dashboardFragment_to_studentListFragment, bundle)
                "Add Batch" -> navigate(R.id.action_dashboardFragment_to_batchFragment, bundle)
                "View Batch" -> navigate(R.id.action_dashboardFragment_to_batchFragment, bundle)
                else -> navigate(R.id.action_dashboardFragment_to_addStudentFragment)
            }
        }
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(requireActivity())
        loadingDialog.showLoadingDialog("Loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }


    private fun callHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }

    private fun logout() {
        findNavController().navigate(R.id.action_profileFragment_to_loginActivity)
    }
}