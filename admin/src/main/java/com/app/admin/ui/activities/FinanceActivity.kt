package com.app.admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.adapters.StudentFinanceAdapter
import com.app.admin.databinding.ActivityFinanceBinding
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.FINANCE_USER
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory


class FinanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinanceBinding
    private lateinit var loadingDialog: LoadingDialog
    private val studentFinanceAdapter = StudentFinanceAdapter()
    private lateinit var viewModel: RetrofitViewModel
    private var loaderShown = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setAdapter()
        fetchStudentData()
        setObserver()
        events()
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
        viewModel = ViewModelProvider(
            this@FinanceActivity,
            RetrofitViewModelFactory(repository)
        )[RetrofitViewModel::class.java]

    }

    private fun setAdapter() {
        with(binding) {
            studentsRecyclerView.layoutManager = LinearLayoutManager(this@FinanceActivity)
            studentsRecyclerView.adapter = studentFinanceAdapter
        }
    }

    private fun fetchStudentData() {
        viewModel.fetchStudents(FINANCE_USER, PickerManager.token!!)
    }

    private fun setObserver() {
        viewModel.students.observe(this@FinanceActivity) { students ->
            studentFinanceAdapter.submitList(students)
            callHandler()
        }
    }

    private fun showLoader() {
        loadingDialog = LoadingDialog(this)
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

}