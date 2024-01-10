package com.example.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenScreen(navController: NavController, userId: Int) {
    var selectedQuantity by remember { mutableStateOf("") }
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf("Kitchen", "History", "Me")

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

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Welcome Image
            Image(
                painter = painterResource(id = R.drawable.welcome),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .offset(y = 100.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            // 选项框
            TextField(
                value = selectedQuantity,
                onValueChange = {
                    // 处理输入的数量
                    selectedQuantity = it
                },
                label = { Text("选择菜品的数量") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clip(RoundedCornerShape(10.dp))
            )

            Spacer(modifier = Modifier.height(15.dp))

            // 按钮
            ElevatedButton(
                onClick = {
                    // 如果输入为空，将其视为默认值
                    val quantity = if (selectedQuantity.isBlank()) "0" else selectedQuantity
                    navController.navigate("dishScreen/$userId/$quantity")
                },
                modifier = Modifier
                    .align(Alignment.End)
                    .width(150.dp)
                    .height(50.dp)
                    .padding(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = Color(0xFFB8B8D0)
                )
            ) {
                Text("开宴")
            }
        }

        // 底部导航栏
        NavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
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
