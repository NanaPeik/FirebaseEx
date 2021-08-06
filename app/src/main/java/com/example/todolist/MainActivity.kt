package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), TodoListAdapter.RecordClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var records:MutableList<RecordDocument>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        records = arrayListOf<RecordDocument>()
        todoListAdapter = TodoListAdapter(records, this)

        val userId = intent.getStringExtra("user_id")

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.add.setOnClickListener {
            if (binding.newNote.text.isNotEmpty()) {
                if (userId != null) {
                    saveFireStore(binding.newNote.text.toString(), userId)
                }
            } else {
                Toast.makeText(this, "fill record..", Toast.LENGTH_LONG).show()
            }

        }
        binding.records.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        if (userId != null) {
            readFireStoreData(userId)
            binding.records.adapter = todoListAdapter
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
        records.clear()


        db.collection("todo")
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        if (document.data.getValue("userId") == userId) {
                            val record = RecordDocument(
                                document.id, document.data.getValue("text").toString(),
                                document.data.getValue("userId").toString()
                            )
                            records.add(record)
                        }
                    }
                    todoListAdapter.notifyDataSetChanged()
                }
            }

    }

    override fun itemClicked(record: RecordDocument) {
        val db = FirebaseFirestore.getInstance()

        db.collection("todo")
            .document(record.documentId)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "record deleted", Toast.LENGTH_LONG).show()
                    todoListAdapter.removeRecord(record)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "failed to delete record", Toast.LENGTH_LONG).show()
            }
    }
}