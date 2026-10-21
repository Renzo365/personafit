package com.personafit.app.data.remote

import okhttp3.ResponseBody
import org.json.JSONObject

object ErrorUtils {

    fun parseError(errorBody: ResponseBody?, fallback: String = "An unexpected error occurred."): String {
        if (errorBody == null) return fallback
        return parseError(errorBody.string(), fallback)
    }

    fun parseError(rawJson: String?, fallback: String = "An unexpected error occurred."): String {
        if (rawJson.isNullOrBlank()) return fallback
        return try {
            val json = JSONObject(rawJson)

            // 1. Check if specific validation errors map exists
            if (json.has("errors")) {
                val errorsObj = json.optJSONObject("errors")
                if (errorsObj != null && errorsObj.length() > 0) {
                    val keys = errorsObj.keys()
                    while (keys.hasNext()) {
                        val key = keys.next()
                        val arr = errorsObj.optJSONArray(key)
                        if (arr != null && arr.length() > 0) {
                            val firstError = arr.optString(0)
                            if (!firstError.isNullOrBlank()) {
                                return cleanMessage(firstError)
                            }
                        }
                    }
                }
            }

            // 2. Check general "message"
            if (json.has("message")) {
                val message = json.optString("message")
                if (!message.isNullOrBlank()) {
                    return cleanMessage(message)
                }
            }

            fallback
        } catch (e: Exception) {
            cleanMessage(rawJson)
        }
    }

    private fun cleanMessage(msg: String): String {
        val trimmed = msg.trim()
        return when {
            trimmed.contains("credentials do not match", ignoreCase = true) ->
                "Incorrect email or password. Please try again."
            trimmed.contains("email field must be a valid email", ignoreCase = true) ->
                "Please enter a valid email address."
            trimmed.contains("password field is required", ignoreCase = true) ->
                "Please enter your password."
            trimmed.contains("email field is required", ignoreCase = true) ->
                "Please enter your email address."
            trimmed.contains("email has already been taken", ignoreCase = true) ->
                "This email is already registered."
            else -> trimmed
        }
    }
}
