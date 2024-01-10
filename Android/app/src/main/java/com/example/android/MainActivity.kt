package com.example.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.android.ui.theme.AndroidTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.android.ViewModel.LoginViewModel

class MainActivity : ComponentActivity() {
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 创建NavController
                    val navController = rememberNavController()

                    // 创建NavHost
                    NavHost(
                        navController = navController,
                        startDestination = "login"
                    ) {
                        // 定义LoginScreen导航
                        composable("login") {
                            LoginScreen(navController = navController)
                        }

                        composable(
                            route = "kitchen/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { entry ->
                            val userId = entry.arguments?.getInt("userId") ?: 0
                            KitchenScreen(navController = navController, userId = userId)
                        }


                        composable(
                            route = "kitchen/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { entry ->
                            val userId = entry.arguments?.getInt("userId") ?: 0
                            KitchenScreen(navController = navController, userId = userId)
                        }

                        composable(
                            route = "history/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { entry ->
                            val userId = entry.arguments?.getInt("userId") ?: 0
                            HistoryScreen(navController = navController, userId = userId)
                        }

                        composable(
                            route = "me/{userId}",
                            arguments = listOf(navArgument("userId") { type = NavType.IntType })
                        ) { entry ->
                            val userId = entry.arguments?.getInt("userId") ?: 0
                            UserDetailScreen(navController = navController, userId = userId)
                        }

                        composable(
                            route = "dishScreen/{userId}/{selectedQuantity}",
                            arguments = listOf(
                                navArgument("userId") { type = NavType.IntType },
                                navArgument("selectedQuantity") { type = NavType.IntType }
                            )
                        ) { entry ->
                            val userId = entry.arguments?.getInt("userId") ?: 0
                            val selectedQuantity = entry.arguments?.getInt("selectedQuantity") ?: 0
                            DishScreen(navController = navController, userId = userId, selectedQuantity = selectedQuantity)
                        }

                    }
                }
            }
        }
    }
}
