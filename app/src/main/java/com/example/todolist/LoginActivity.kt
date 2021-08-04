package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.todolist.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToRegistration.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.loginEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email.", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(binding.loginPass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val email: String = binding.loginEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.loginPass.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                Toast.makeText(
                                    this,
                                    "You are logged in successfully.",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", FirebaseAuth.getInstance().currentUser?.uid)
//                                intent.putExtra("email_id", email)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this,
                                    task.exception?.message.toString(),
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }
        }
    }
}