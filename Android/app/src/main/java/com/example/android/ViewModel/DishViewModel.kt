package com.example.android.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.Dish
import com.example.android.DishStatisticsResponse
import com.example.android.Ingredient
import com.example.android.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//class DishViewModel : ViewModel() {
//    // 使用 MutableStateFlow 来存储 API 请求结果
//    private val _result = MutableStateFlow<DishStatisticsResponse?>(null)
//    val result: StateFlow<DishStatisticsResponse?> get() = _result.asStateFlow()
//
//    fun postDishData(dishes: List<Dish>) {
//        viewModelScope.launch {
//            try {
//                // 调用 Retrofit 接口
//                _result.value = RetrofitClient.apiService.postDishData(dishes)
//            } catch (e: Exception) {
//                // 处理异常
//                e.printStackTrace()
//                _result.value = null
//            }
//        }
//    }
//}
