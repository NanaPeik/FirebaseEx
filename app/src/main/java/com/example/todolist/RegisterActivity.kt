package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todolist.databinding.ActivityRegisterBinding
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
            register(
                binding.registerEmail.text.toString().trim { it <= ' ' },
                binding.registerPass.text.toString().trim { it <= ' ' }
            )
        }
    }

    private fun register(email: String, password: String) {
        val validateData = validator.checkPasswordAndEmailValidation(email, password)

        if (validateData) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        val firebaseUser: FirebaseUser? = task.result?.user

                        toast("You were registered successfully.")

                        val intent = Intent(this, MainActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        intent.putExtra("user_id", firebaseUser?.uid)
                        startActivity(intent)
                        finish()
                    } else {
                        toast(task.exception?.message.toString())
                    }
                }
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}