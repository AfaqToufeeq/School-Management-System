package com.app.admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.adapters.NewsAdapter
import com.app.admin.adapters.StudentFinanceAdapter
import com.app.admin.databinding.ActivityFinanceBinding
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.FINANCE_USER
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager
import com.app.admin.utils.USER_TYPE
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import java.util.Timer
import kotlin.concurrent.scheduleAtFixedRate


class FinanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinanceBinding
    private lateinit var loadingDialog: LoadingDialog
    private val studentFinanceAdapter = StudentFinanceAdapter()
    private lateinit var viewModel: RetrofitViewModel
    private var  newsAdapter = NewsAdapter()
    private val timer = Timer()
    private var loaderShown = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        fetchData()
        setAdapter()
        fetchStudentData()
        setObserver()
        events()
    }

    private fun fetchData() {
        fetchNews()
    }

    private fun fetchNews() {
        viewModel.getNewsEvents(USER_TYPE, PickerManager.token!! )
    }


    private fun events() {
        binding.logOut.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun init() {
        if (!loaderShown) {
            showLoader()
            loaderShown = true
        }

        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(this@FinanceActivity, RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]

    }

    private fun newEventsObserver() {
        viewModel.getNews.observe(this) { news->
            if (!news.isNullOrEmpty()) {
                newsAdapter.submitList(news)
                callHandler()
            }
        }
    }

    private fun setAdapter() {
        with(binding) {
            setTimerToScroll(
                LinearLayoutManager(this@FinanceActivity, LinearLayoutManager.HORIZONTAL,
                    false
                ).also { viewPagerDashboard.layoutManager = it }
            )

            dashboardRecyclerView.layoutManager = LinearLayoutManager(this@FinanceActivity)
            dashboardRecyclerView.adapter = studentFinanceAdapter
            viewPagerDashboard.adapter = newsAdapter
        }
    }

    private fun setObserver() {
        newEventsObserver()
        studentObserver()
    }

    private fun studentObserver() {
        viewModel.students.observe(this@FinanceActivity) { students ->
            studentFinanceAdapter.submitList(students)
            callHandler()
        }
    }

    private fun fetchStudentData() {
        viewModel.fetchStudents(FINANCE_USER, PickerManager.token!!)
    }

    private fun setTimerToScroll(layoutManager: LinearLayoutManager) {
        // Schedule automatic scrolling every 5 seconds
        timer.scheduleAtFixedRate(5 * 1000, 5 * 1000) {
            runOnUiThread {
                val currentPosition = layoutManager.findFirstVisibleItemPosition()
                val nextPosition = if (currentPosition < layoutManager.itemCount - 1) currentPosition + 1 else 0
                binding.viewPagerDashboard.smoothScrollToPosition(nextPosition)
            }
        }
    }


    private fun showLoader() {
        loadingDialog = LoadingDialog(this)
        loadingDialog.showLoadingDialog("Loading, Please wait...")
    }

    private fun hideLoader() {
        loadingDialog.dismissLoadingDialog()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }

    private fun callHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            hideLoader()
        }, 1500)
    }

}