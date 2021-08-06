package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todolist.databinding.ActivityRegisterBinding

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