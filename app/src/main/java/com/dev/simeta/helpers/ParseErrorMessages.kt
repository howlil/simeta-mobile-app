package com.dev.simeta.helpers

import org.json.JSONArray
import org.json.JSONObject

 fun parseErrorMessages(jsonString: String): List<String> {
    return try {
        val jsonObject = JSONObject(jsonString)
        if (jsonObject.has("message")) {
            val message = jsonObject.get("message")
            if (message is JSONArray) {
                // If the message is an array, convert each item to a string
                (0 until message.length()).map { index -> message.getString(index) }
            } else {
                listOf(message.toString())
            }
        } else {
            listOf("Unknown error occurred.")
        }
    } catch (e: Exception) {
        listOf("Error parsing server response.")
    }
}