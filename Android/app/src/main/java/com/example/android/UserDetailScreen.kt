package com.example.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import androidx.compose.material3.NavigationBar
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

@Composable
fun UserDetailScreen(navController: NavController, userId: Int) {
    var userData by remember { mutableStateOf<UserData?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(2) }

    val items = listOf("Kitchen", "History", "Me")

    LaunchedEffect(userId) {
        coroutineScope.launch {
            try {
                userData = RetrofitClient.apiService.getUserData(userId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background),
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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (userData != null) {
                // 显示用户数据
                userData!!.let { user ->
                    userField("姓名", user.name)
                    userField("昵称", user.nickname)
                    userField("性别", user.sex)
                    userField("账号", user.account)
                    userField("手机号", user.phone)
                }
            } else {
                // 显示加载中或错误信息
                Text("Loading user data...")
            }
        }

        // 底部导航栏
        NavigationBar(
            modifier = Modifier
                .align(BottomCenter)
                .fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        when (index) {
                            0 -> Icon(Icons.Filled.Favorite, contentDescription = item)
                            1 -> Icon(Icons.Filled.DateRange, contentDescription = item)
                            2 -> Icon(Icons.Filled.Person, contentDescription = item)
                            else -> throw IllegalArgumentException("Invalid index: $index")
                        }
                    },
                    label = { Text(item) },
                    selected = selectedItem == index,
                    onClick = {
                        selectedItem = index
                        // 处理点击事件，比如导航到不同的目标，并传递用户 ID
                        when (index) {
                            0 -> navController.navigate("kitchen/$userId")
                            1 -> navController.navigate("history/$userId")
                            2 -> navController.navigate("me/$userId")
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun userField(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$key:", fontWeight = FontWeight.Bold)
        Text(text = value)
    }
}
