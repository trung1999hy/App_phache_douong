package com.example.recipeinstructions.utils

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.recipeinstructions.model.Beverage
import com.example.recipeinstructions.model.Instruction
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.nio.charset.StandardCharsets


object Constants {
    var IS_EMPTY = ""
    const val KEY_COIN = "KEY_COIN_5i" // 5 coin

    const val KEY_10_COIN = "key_in_app_10i" // 100 coin

    const val KEY_20_COIN = "key_in_app_20i" // 150 coin

    const val KEY_50_COIN = "key_in_app_50i" // 300 coin

    const val KEY_100_COIN = "key_in_app_100i" // 500 coin

    const val KEY_150_COIN = "key_in_app_150i" // 700 coin

    const val KEY_200_COIN = "key_in_app_200i" // 999 coin

    const val KEY_500_COIN = "key_in_app_500i"

    const val KEY_USD_NEWS = "KEY_USD_NEWS_5i" // 5 coin

    const val KEY_10_USD_NEWS = "key_USD_NEWS_10i" // 100 coin

    const val KEY_20_USD_NEWS = "key_USD_NEWS_20i" // 150 coin

    const val KEY_50_USD_NEWS = "key_USD_NEWS_50i" // 300 coin

    const val KEY_100_USD_NEWS = "key_USD_NEWS_100i" // 500 coin

    const val KEY_150_USD_NEWS = "key_USD_NEWS_150i" // 700 coin

    const val KEY_200_USD_NEWS = "key_USD_NEWS_200i" // 999 coin

    const val KEY_500_USD_NEWS = "key_USD_NEWS_500i"
    fun loadJSONFromAsset(context: Context, fileName: String): JSONArray? {
        val json: String?
        try {
            Log.d(TAG, "loadJSONFromAsset:${context.assets.open(fileName)} ")
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            json = String(buffer, StandardCharsets.UTF_8)
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return try {
            JSONArray(json)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun parseJSONToListInstruction(jsonArray: JSONArray): List<Instruction> {
        val drinkList = mutableListOf<Instruction>()
        for (i in 0 until jsonArray.length()) {
            val jsonDrink = jsonArray.getJSONObject(i)
            val idObject = jsonDrink.getJSONObject("_id")
            val id = idObject.getString("\$oid")
            val typeDrink = jsonDrink.getString("type_drink")
            val title = jsonDrink.getString("title")
            val image = jsonDrink.getString("image")
            val ingredientArray = jsonDrink.getJSONArray("ingredient")
            val instructionArray = jsonDrink.getJSONArray("intructions")

            val ingredients = mutableListOf<String>()
            val instructions = mutableListOf<String>()

            for (j in 0 until ingredientArray.length()) {
                ingredients.add(ingredientArray.getString(j))
            }

            for (j in 0 until instructionArray.length()) {
                instructions.add(instructionArray.getString(j))
            }

            val drink = Instruction(id, typeDrink, title, image, ingredients, instructions)
            drinkList.add(drink)
        }

        return drinkList
    }
    fun parseJSONToListType(jsonArray: JSONArray): ArrayList<Beverage> {
        val drinkList = arrayListOf<Beverage>()
        for (i in 0 until jsonArray.length()) {
            val jsonDrink = jsonArray.getJSONObject(i)
            val idObject = jsonDrink.getJSONObject("_id")
            val id = idObject.getString("\$oid")
            val type = jsonDrink.getString("type")
            val image = jsonDrink.getString("image")
            val drink = Beverage(id, type, image)
            drinkList.add(drink)
        }

        return drinkList
    }



}