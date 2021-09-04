package com.homework.nasibullintinkoff.utils

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast

object Utility {
    private const val ERROR_MESSAGE =  "Error"

    /**
     * toast with null string handling
     */
    fun showToast(message: String?, context:Context?) {
        Log.e("Toast", message?:"")
        when {
            message.isNullOrEmpty() -> {
                showToast(ERROR_MESSAGE, context)
            }
            else -> Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
}