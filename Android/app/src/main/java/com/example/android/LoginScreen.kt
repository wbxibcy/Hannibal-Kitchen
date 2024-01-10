package com.example.android

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.android.ViewModel.LoginViewModel
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.example.android.ui.theme.PurpleGrey80
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel = viewModel(), navController: NavController) {
    var account by remember { mutableStateOf("zhangsan@123.com") }
    var password by remember { mutableStateOf("password123") }

    val userIdState by loginViewModel.userId.collectAsState()

    var accountshowSnackbar by remember { mutableStateOf(false) }
    var idshowSnackbar by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.login_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        // Existing Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            // Add an image at the top
            Image(
                painter = painterResource(id = R.drawable.move_little_kitchen),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .offset(y = (-20).dp)
            )

            OutlinedTextField(
                value = account,
                onValueChange = {account = it},
                label = { Text("账号") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .offset(y = (-10).dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFB8B8D0), // 激活状态的边框颜色
                    textColor = Color.Black // 文本颜色
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                })
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("密码") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .offset(y = (-10).dp),
                visualTransformation = PasswordVisualTransformation(),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color(0xFFB8B8D0), // 激活状态的边框颜色
                    textColor = Color.Black // 文本颜色
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    // Perform the login action
                    loginViewModel.login(account, password)
                })
            )

            Spacer(modifier = Modifier.height(30.dp))

            ElevatedButton(
                onClick = {
                    // 在这里检查账号是否为合法的邮箱地址
                    if (isValidEmail(account)) {
                        // 执行登录操作
                        loginViewModel.login(account, password)

                        // 获取当前的用户ID
                        val userId = userIdState

                        println("userId: $userId")

                        if (userId != 0) {
                            println("last id: $userId, type: ${userId::class.java.simpleName}")

                            navController.navigate("kitchen/$userId")

                        } else {
                            idshowSnackbar = true
                        }
                    } else {
                        accountshowSnackbar = true
                    }
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(200.dp)
                    .height(50.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = Color(0xFFB8B8D0)
                )
            ) {
                Text("Login")
            }
        }

        if (idshowSnackbar) {
            LaunchedEffect(idshowSnackbar) {
                // 等待一段时间后隐藏Snackbar
                delay(3000)
                idshowSnackbar = false
            }

            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text("账号或者密码错误，请重新输入")
            }
        }

        if (accountshowSnackbar) {
            LaunchedEffect(accountshowSnackbar) {
                // 等待一段时间后隐藏Snackbar
                delay(3000)
                accountshowSnackbar = false
            }

            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {
                Text("账号格式有问题，请输入合法的邮箱地址")
            }
        }

    }
}

private fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\S+@\\S+\\.\\S+\$")
    return emailRegex.matches(email)
}
