package com.example.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.android.RetrofitClient.apiService
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController, userId: Int) {
    var searchText by remember { mutableStateOf("") }
    var logData by remember { mutableStateOf<List<LogData>>(emptyList()) }
    var selectedItem by remember { mutableStateOf(1) }
    var isDialogVisible by remember { mutableStateOf(false) }
    var searchData by remember { mutableStateOf<List<LogData>>(emptyList()) }

    val items = listOf("Kitchen", "History", "Me")

    LaunchedEffect(userId) {
        try {
            logData = apiService.getLog(userId)
        } catch (e: Exception) {
            // 处理异常
            e.printStackTrace()
        }
    }

    LaunchedEffect(userId, searchText) {
        try {
            searchData = apiService.searchLog(userId, searchText)
        } catch (e: Exception) {
            // 处理异常
            e.printStackTrace()
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

        // 底部导航栏
        NavigationBar(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
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

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // 搜索框
            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                trailingIcon = {
                    // 添加搜索按钮
                    IconButton(onClick = {
                        // 显示对话框
                        isDialogVisible = true
                    }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black, // 激活状态的边框颜色
                    textColor = Color.Black // 文本颜色
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                shape = RoundedCornerShape(8.dp)
            )

            // 分隔线
            Spacer(modifier = Modifier.height(16.dp))

            // 历史数据列表
            LazyColumn {
                items(logData.size) { index ->
                    HistoryItem(logData[index].dish_name)
                }
            }

            // 展示搜索结果的对话框
            if (isDialogVisible) {
                SearchResultsDialog(
                    searchResults = searchData,
                    onDismiss = { isDialogVisible = false }
                )
            }
        }
    }
}

@Composable
fun SearchResultsDialog(searchResults: List<LogData>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("搜索结果") },
        text = {
            LazyColumn {
                items(searchResults.size) { index ->
                    Text(searchResults[index].dish_name)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("OK")
            }
        }
    )
}

@Composable
fun HistoryItem(name: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.small,
        color = Color.Transparent,
        content = {
            Text(
                text = name,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    )
}