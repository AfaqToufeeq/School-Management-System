package com.attech.sms.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.attech.sms.R
import com.attech.sms.databinding.ActivityMainBinding
import com.attech.sms.network.RetrofitClientInstance
import com.attech.sms.repository.RetrofitRepository
import com.attech.sms.ui.fragments.DashboardFragment
import com.attech.sms.utils.PickerManager.studentData
import com.attech.sms.utils.PickerManager.token
import com.attech.sms.utils.PickerManager.userName
import com.attech.sms.utils.USER_TYPE
import com.attech.sms.viewmodel.RetrofitViewModel
import com.attech.sms.viewmodelfactory.RetrofitViewModelFactory


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

}