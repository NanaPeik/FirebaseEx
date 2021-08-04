package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.todolist.databinding.ActivityRegisterBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.goToLogin.setOnClickListener {
            onBackPressed()
        }

        binding.registerButton.setOnClickListener {
            when {
                TextUtils.isEmpty(binding.registerEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email.", Toast.LENGTH_LONG).show()
                }
                TextUtils.isEmpty(binding.registerPass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val email: String = binding.registerEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.registerPass.text.toString().trim { it <= ' ' }

                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val firebaseUser: FirebaseUser? = task.result?.user

                                Toast.makeText(
                                    this,
                                    "You were registered successfully.",
                                    Toast.LENGTH_LONG
                                ).show()

                                val intent = Intent(this, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                intent.putExtra("user_id", firebaseUser?.uid)
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