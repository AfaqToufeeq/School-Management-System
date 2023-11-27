package com.app.admin.ui.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.admin.R
import com.app.admin.adapters.FeeDetailAdapter
import com.app.admin.databinding.ActivityFinanceViewFeeBinding
import com.app.admin.models.FeeDetailModel
import com.app.admin.models.FeeDetailResponse
import com.app.admin.models.FeeStudent
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.FINANCE_USER
import com.app.admin.utils.ImageUtil
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager.token
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import java.util.Calendar

class FinanceViewFeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFinanceViewFeeBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: RetrofitViewModel
    private var selectedDate: String = ""
    private lateinit var feeStudent: FeeStudent
    private var feeDetailAdapter = FeeDetailAdapter()
    private var filteredData: List<FeeDetailResponse> = emptyList()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceViewFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        setAdapter()
        getIntentData()
        events()
        setObserver()
    }

    private fun setAdapter() {
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = feeDetailAdapter
    }

    private fun setObserver() {
        viewModel.feeDetails.observe(this) { fee->
            if (!fee.isNullOrEmpty()) {
                feeDetailAdapter.setFeeDetailList(fee)
                filteredData = fee
                callHandler()
            }
        }
    }

    private fun events() {
        binding.datePickerButton.setOnClickListener { showDatePicker() }
        binding.leftIcon.setOnClickListener { finish() }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar[Calendar.YEAR]
        val month = calendar[Calendar.MONTH]
        val day = calendar[Calendar.DAY_OF_MONTH]

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                selectedDate = String.format(
                    "%04d-%02d-%02d",
                    selectedYear,
                    selectedMonth + 1, // Month is 0-based
                    selectedDay
                )
                binding.selectedDate.text = selectedDate
                filterRecyclerView(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun filterRecyclerView(selectedDate: String) {
        feeDetailAdapter.filterByDate(selectedDate)
        val filteredData = getFilteredData(selectedDate)
        if (filteredData.isEmpty()) {
            Toast.makeText(this, "No records found for $selectedDate", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getFilteredData(selectedDate: String): List<FeeDetailResponse> {
        return filteredData.filter { it.date == selectedDate }
    }


    private fun getIntentData() {
        feeStudent = intent.getSerializableExtra("student") as FeeStudent
        feeStudent?.let { feeStudent ->
            with(binding) {
                textViewStudentName.text = "${feeStudent.firstname} ${feeStudent.lastname}"
                textViewRollNumber.text = "Roll No. ${feeStudent.rollno}"
                if (feeStudent.image != null)
                    studentImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(feeStudent.image))
                else
                    studentImageView.setImageResource(R.drawable.student_icon)
            }

            fetchFeeDetails(feeStudent.studentID)
        }
    }

    private fun fetchFeeDetails(studentID: Int) {
        viewModel.fetchFeeDetails(
            FeeDetailModel(
                type = FINANCE_USER,
                token = token!!,
                student = studentID
            )
        )
    }

    private fun init() {
        showLoader()
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(
            this,
            RetrofitViewModelFactory(repository)
        )[RetrofitViewModel::class.java]

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