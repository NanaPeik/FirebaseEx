package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.todolist.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var validator: CheckEmailAndPasswordValidation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        validator = CheckEmailAndPasswordValidation()
        binding.goToLogin.setOnClickListener {
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            validator.checkPasswordAndEmailValidation(
                binding.registerEmail.text.toString().trim { it <= ' ' },
                binding.registerPass.text.toString().trim { it <= ' ' }
            )
        }
    }

}