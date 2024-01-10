package com.example.android

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    val account: String,
    val password: String
)

data class LoginResponse(
    val user_id: Int
)

data class UserData(
    val user_id: Int,
    val name: String,
    val nickname: String,
    val sex: String,
    val account: String,
    val phone: String
)

data class LogData(
    val dish_name: String
)

//data class DishRequest(
//    @SerializedName("user_id")
//    val userId: Int,
//    val dishes: List<Dish>
//)

data class Dish(
    @SerializedName("dish_name")
    val dishName: String,
    val ingredients: Map<String, Ingredient>
)

data class Ingredient(
    val ingredientName: String,
    val quantity: Int,
    val unit: String
)

data class DishStatisticsResponse(
    @SerializedName("ingredient_details")
    val ingredientDetails: Map<String, IngredientDetails>
)

data class IngredientDetails(
    val quantity: Int,
    val unit: String
)

data class DishBlockState(
    var dishName: String,
    var ingredients: List<Ingredient>
) {
    fun copyIngredient(index: Int, ingredientName: String, quantity: Int, unit: String): DishBlockState {
        val updatedIngredients = ingredients.toMutableList()
        updatedIngredients[index] = Ingredient(ingredientName, quantity, unit)
        return copy(ingredients = updatedIngredients)
    }

    fun copyIngredients(newIngredients: List<Ingredient>): DishBlockState {
        return copy(ingredients = newIngredients)
    }
}
