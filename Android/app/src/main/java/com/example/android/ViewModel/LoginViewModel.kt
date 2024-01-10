package com.example.android.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.android.LoginRequest
import com.example.android.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _userId = MutableStateFlow(0)
    val userId: StateFlow<Int> get() = _userId.asStateFlow()

    fun login(account: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.login(LoginRequest(account, password))
                _userId.value = response.user_id

                // 打印获取到的用户ID
                println("User ID: ${response.user_id}")

            } catch (e: Exception) {
                // 处理异常
                e.printStackTrace()
            }
        }
    }
}
