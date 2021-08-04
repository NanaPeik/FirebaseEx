package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userId = intent.getStringExtra("user_id")

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.add.setOnClickListener {
            if (userId != null) {
                saveFireStore(binding.newNote.text.toString(), userId)
            }
        }
        if (userId != null) {
            readFireStoreData(userId)
        }
    }

    private fun saveFireStore(noteText: String, userId: String) {
        val db = FirebaseFirestore.getInstance()
        val note: MutableMap<String, Any> = HashMap()
        note["text"] = noteText
        note["userId"] = userId

        db.collection("todo")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "record added successfully", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "record failed to add", Toast.LENGTH_LONG).show()

            }
        readFireStoreData(userId)
    }

    private fun readFireStoreData(userId: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("todo")
            .get()
            .addOnCompleteListener {
                val result: StringBuffer = StringBuffer()

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        if (document.data.getValue("userId") == userId) {
                            result.append(document.data.getValue("text")).append("\n\n")
                        }
//                                                    .append(document.data.getValue("userId")).append("\n\n")
                    }
                    binding.noteListItem.text = result
                }
            }
    }
}