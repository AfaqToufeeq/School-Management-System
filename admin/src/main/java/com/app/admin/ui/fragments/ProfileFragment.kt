package com.app.admin.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.admin.R
import com.app.admin.databinding.FragmentProfileBinding
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.utils.MAIN_MENU
import com.app.admin.utils.PickerManager.studentData
import com.app.admin.utils.PickerManager.token
import com.app.admin.utils.USER_TYPE
import com.app.admin.utils.Utils
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private var title: String = ""
    private lateinit var viewModel: RetrofitViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = arguments?.getString(MAIN_MENU) ?: ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment using view binding
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        setData()
        events()
    }

    private fun init() {
        binding.smsText.text = title
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(requireActivity(), RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun events() {
        binding.buttonLogout.setOnClickListener { logout() }
        binding.leftIcon.setOnClickListener { findNavController().popBackStack() }
    }


    private fun setData() {
        with(binding) {
            studentData?.let{
                imageViewProfile.setImageResource(R.drawable.profile_icon)
                textViewFullName.text = "${it.firstname} ${it.lastname}"
                textViewRollNo.text = it.rollno
                textViewContact.text = it.contact
                textViewNic.text = it.nic
                textViewAddress.text = it.address
                textViewUsername.text = it.username
            }
        }
    }

    private fun logout() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    Log.d("Status", "logout token $token")

                    viewModel.logout(USER_TYPE, token!!)
                }
                if (response.msg.isNotEmpty()) {
                    navigateToLogin()
                    Log.d("Status", "logout ${response.msg}")
                    Utils.showToast(requireActivity(), response.msg)
                } else {
                    Utils.showToast(requireActivity(), "Logout failed")
                }
            } catch (e: Exception) {
                Utils.showToast(requireActivity(), "Logout failed: ${e.message}")
            } finally {
                showLoading(false)
            }
        }
    }

    private fun navigateToLogin() {
       findNavController().navigate(R.id.action_profileFragment_to_loginActivity)
    }

    private fun showLoading(show: Boolean) {
//        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }
}
