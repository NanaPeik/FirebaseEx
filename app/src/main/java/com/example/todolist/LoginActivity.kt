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

        //Todo: onCreate დან გაიტანე ეს ლოგიკა ფუნქციებად, ვიუმოდელში მთლად უკეთესი იქნებოდა
        binding.registerButton.setOnClickListener {
            when {
                //Todo: it <= ' ' } ამას რატომ აკეთებ ვერ მივხვდი , აქ რეგექსით უნდა შეამოწმო მეილი სწორი ფორმატისაა თუ არა
                TextUtils.isEmpty(binding.loginEmail.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter email.", Toast.LENGTH_LONG).show()
                }
                //Todo: პაროლსაც რაიმე შეზღუდვები დაუდე
                TextUtils.isEmpty(binding.loginPass.text.toString().trim { it <= ' ' }) -> {
                    Toast.makeText(this, "Please enter password.", Toast.LENGTH_LONG).show()
                }
                else -> {
                    val email: String = binding.loginEmail.text.toString().trim { it <= ' ' }
                    val password: String = binding.loginPass.text.toString().trim { it <= ' ' }

                    //Todo:ლოადერები საჭიროა ყველგან
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                //Todo: error ის ჩვენება ცალკე ფუნქციად რომ გქონდეს და ყველგან გამოიყენებდი, ექსთენშენადაც შეიძლება
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