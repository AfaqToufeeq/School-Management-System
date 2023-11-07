package com.app.admin.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.admin.databinding.ActivityLoginBinding
import com.app.admin.interfaces.ApiService
import com.app.admin.network.RetrofitClientInstance
import com.app.admin.repository.RetrofitRepository
import com.app.admin.viewmodel.RetrofitViewModel
import com.app.admin.viewmodelfactory.RetrofitViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: RetrofitViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        buttonClicks()

    }

    private fun init() {
        val repository = RetrofitRepository(RetrofitClientInstance.retrofit)
        viewModel = ViewModelProvider(this, RetrofitViewModelFactory(repository))[RetrofitViewModel::class.java]
    }

    private fun buttonClicks() {
        binding.btnLogin.setOnClickListener {
            val type = "admin"
            val username = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString()

            lifecycleScope.launch {
                try {
                    val token = withContext(Dispatchers.IO) {
                        viewModel.login(type, username, password)
                    }
                    if (!token.token.isNullOrEmpty()) {
                        Log.d("Login","Success")
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                    } else {
                        Log.d("Login","Failed")
                    }
                } catch (e: Exception) {
                    Log.d("Login","Error $e")
                }
            }
        }
    }
}