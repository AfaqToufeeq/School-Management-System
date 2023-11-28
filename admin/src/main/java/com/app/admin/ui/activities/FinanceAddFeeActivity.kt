package com.app.admin.ui.activities

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.admin.R
import com.app.admin.databinding.ActivityFinanceAddFeeBinding
import com.app.admin.models.FeeModel
import com.app.admin.models.FeeResponse
import com.app.admin.models.FeeStudent
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.FINANCE_USER
import com.app.admin.utils.ImageUtil
import com.app.admin.utils.LoadingDialog
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.Utils.showToast
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.Calendar

class FinanceAddFeeActivity : AppCompatActivity() {
    private lateinit var feeStudent: FeeStudent
    private lateinit var binding: ActivityFinanceAddFeeBinding
    private lateinit var loadingDialog: LoadingDialog
    private lateinit var viewModel: RetrofitViewModel
    private var selectedDate: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinanceAddFeeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        getIntentData()
        events()
    }

    private fun events() {
        binding.datePickerButton.setOnClickListener { showDatePicker() }
        binding.addFeeBtn.setOnClickListener {
            if (!selectedDate.isNullOrEmpty()) {
                showLoader()
                addFee()
            } else {
                showToast(this, "Please Select data")
            }
        }

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
                selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                binding.selectedDate.text = selectedDate
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }


    private fun getIntentData() {
        feeStudent = intent.getSerializableExtra("student") as FeeStudent
        feeStudent?.let {  feeStudent->
            with(binding) {
                textViewStudentName.text = "${feeStudent.firstname} ${feeStudent.lastname}"
                textViewRollNoDetails.text = "Roll No. ${feeStudent.rollno}"
                textViewContact.text = "Contact: ${feeStudent.contact}"
                textViewNIC.text = "NIC: ${feeStudent.nic}"
                textViewAddress.text = "Address: ${feeStudent.address}"
                if (feeStudent.image!=null)
                    studentImageView.setImageBitmap(ImageUtil.decodeBase64ToBitmap(feeStudent.image))
                else
                    studentImageView.setImageResource(R.drawable.student_icon)
            }
        }
    }

    private fun init() {
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(this, RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun addFee() {
        feeStudent?.let { fee->
            lifecycleScope.launch {
                val response = withContext(Dispatchers.IO) {
                    viewModel.payFee(
                        FeeModel(
                            type = FINANCE_USER,
                            token = token!!,
                            student = fee.studentID,
                            date = selectedDate
                        )
                    )
                }
                logResponse(response)
            }
        }
    }

    private fun logResponse(response: Response<FeeResponse>) {
        if (response.isSuccessful) {
            showToast(this, "Fee Status Updated")
            callHandler()
        } else {
            callHandler()
            Log.d("ErrorLogResponse", "Some Issues: ${response.errorBody()!!} ${response.code()}")
            callHandler()
            showToast(this, "Failed to change fee status")
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