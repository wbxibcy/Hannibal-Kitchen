package com.example.android

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun DishScreen(navController: NavController, userId: Int, selectedQuantity: Int) {
    var isDialogVisible by remember { mutableStateOf(false) }
    var dishBlockStates by remember { mutableStateOf(List(selectedQuantity) { DishBlockState("", listOf(Ingredient("", 0, ""))) }) }
    var totalResult by remember { mutableStateOf(emptyMap<Triple<String, Int, String>, Int>()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.background), // 替换成你的背景图片资源ID
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            // 使用LazyColumn根据selectedQuantity生成多个文本块
            LazyColumn {
                items(selectedQuantity) { index ->
                    DishBlock(
                        index + 1,
                        dishBlockStates[index]
                    ) { dishBlockState ->
                        dishBlockStates =
                            dishBlockStates.toMutableList().also { it[index] = dishBlockState }
                    }
                }
            }

            Button(
                onClick = {
                    // 点击统计按钮时，将所有文本块中的食材名称和数量累加
                    val result = dishBlockStates.flatMap { it.ingredients }
                        .groupBy { Triple(it.ingredientName, it.quantity, it.unit) }
                        .mapValues { entry -> entry.value.sumOf { it.quantity } }

                    // 显示对话框
                    totalResult = result
                    isDialogVisible = true
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(150.dp)
                    .height(50.dp)
                    .padding(5.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    contentColor = Color(0xFFB8B8D0)
                )
            ) {
                Text("统计")
            }

            // 显示对话框
            if (isDialogVisible) {
                // 使用 totalResult 显示对话框内容
                showResultDialog(totalResult, onDismiss = { isDialogVisible = false })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DishBlock(
    blockNumber: Int,
    onDishDataChanged1: DishBlockState,
    onDishDataChanged: (DishBlockState) -> Unit
) {
    var dishBlockState by remember { mutableStateOf(DishBlockState("", listOf(Ingredient("", 0, "")))) }

    // 每个文本块
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // 菜品名称的文本框
        OutlinedTextField(
            value = dishBlockState.dishName,
            onValueChange = {
                dishBlockState = dishBlockState.copy(dishName = it)
                onDishDataChanged(dishBlockState)
            },
            label = { Text("菜名") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // 左右并排的文本输入框，用于输入食材名、数量和单位
        dishBlockState.ingredients.forEachIndexed { index, ingredient ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    value = ingredient.ingredientName,
                    onValueChange = {
                        // 更新对应索引的食材名
                        dishBlockState = dishBlockState.copyIngredient(index, it, ingredient.quantity, ingredient.unit)
                        onDishDataChanged(dishBlockState)
                    },
                    label = { Text("食材名") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = ingredient.quantity.toString(),
                    onValueChange = {
                        // 更新对应索引的数量
                        dishBlockState = dishBlockState.copyIngredient(index, ingredient.ingredientName, it.toIntOrNull() ?: 0, ingredient.unit)
                        onDishDataChanged(dishBlockState)
                    },
                    label = { Text("数量") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                OutlinedTextField(
                    value = ingredient.unit,
                    onValueChange = {
                        // 更新对应索引的单位
                        dishBlockState = dishBlockState.copyIngredient(index, ingredient.ingredientName, ingredient.quantity, it)
                        onDishDataChanged(dishBlockState)
                    },
                    label = { Text("单位") },
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
            }
        }

        // 加号和减号按钮
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = {
                // 点击加号按钮，增加一组左右并排的文本输入框
                dishBlockState = dishBlockState.copyIngredients(dishBlockState.ingredients + Ingredient("", 0, ""))
                onDishDataChanged(dishBlockState)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }

            IconButton(onClick = {
                // 点击减号按钮，减少一组左右并排的文本输入框
                if (dishBlockState.ingredients.size > 1) {
                    dishBlockState = dishBlockState.copyIngredients(dishBlockState.ingredients.dropLast(1))
                    onDishDataChanged(dishBlockState)
                }
            }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

@Composable
fun showResultDialog(result: Map<Triple<String, Int, String>, Int>, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("统计结果") },
        text = {
            Column {
                result.forEach { (ingredient, quantity) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        // 将单位信息添加到显示食材的文本中
                        Text("食材: ${ingredient.first}", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("数量: $quantity ${ingredient.third}", fontWeight = FontWeight.Bold)
                    }
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

