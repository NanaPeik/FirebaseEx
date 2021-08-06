package com.example.todolist

import android.text.TextUtils
import android.util.Patterns

class CheckEmailAndPasswordValidation {

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String?): Boolean {
        password?.let {
            val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{4,}$"
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        } ?: return false
    }

    fun checkPasswordAndEmailValidation(email: String, pass: String): Boolean {
        val emailValidation = isValidEmail(email)
        val passwordValidation = isValidPassword(pass)
        return emailValidation && passwordValidation
    }
}