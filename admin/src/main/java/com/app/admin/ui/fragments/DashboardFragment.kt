package com.app.admin.ui.fragments

import android.os.Bundle
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
import com.app.admin.repository.AdminRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.viewmodel.AdminViewModel
import com.app.admin.viewmodelfactory.AdminViewModelFactory
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class DashboardFragment : Fragment(), OnItemClick {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var studentViewModel: AdminViewModel
    private lateinit var dashboardAdapter: DashboardAdapter
    private lateinit var newsAdapter: NewsAdapter
    private val timer = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setObservers()
        setRecyclerView()
        events()
    }

    private fun events() {
        binding.profileCL.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment,
                Bundle().apply { putString(MAIN_MENU, "Profile View")})
        }
    }

    private fun setObservers() {
        studentViewModel.apply {
            dashboardItemsLiveData.observe(viewLifecycleOwner) {
                dashboardAdapter.submitList(it)
            }

            newsItemsLiveData.observe(viewLifecycleOwner) {
                newsAdapter.submitList(it)
            }
        }
    }

    private fun init() {
        val repository = AdminRepository()
        studentViewModel = ViewModelProvider(this, AdminViewModelFactory(repository))[AdminViewModel::class.java]
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
            newsAdapter = NewsAdapter()
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
            }
        }
    }
}