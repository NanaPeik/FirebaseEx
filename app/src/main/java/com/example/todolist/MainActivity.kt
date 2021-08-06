package com.example.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), TodoListAdapter.RecordClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoListAdapter: TodoListAdapter
    private lateinit var records: MutableList<RecordDocument>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        records = arrayListOf()
        todoListAdapter = TodoListAdapter(records, this)

        val userId = intent.getStringExtra("user_id")

        binding.logOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
        binding.add.setOnClickListener {
            if (binding.newNote.text.isNotEmpty() && userId != null) {
                saveFireStore(binding.newNote.text.toString(), userId)
            } else {
                toast("fill record..")
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
                toast("record added successfully")
            }
            .addOnFailureListener {
                toast("record failed to add")

            }
        readFireStoreData(userId)
    }


    private fun readFireStoreData(userId: String) {
        val db = FirebaseFirestore.getInstance()
        records.clear()
        binding.recordsLoader.visibility = View.VISIBLE


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
                    binding.recordsLoader.visibility = View.GONE
                }
            }
    }

    override fun itemClicked(record: RecordDocument) {
        val db = FirebaseFirestore.getInstance()

        binding.recordsLoader.visibility = View.VISIBLE

        db.collection("todo")
            .document(record.documentId)
            .delete()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    toast("record deleted")
                    todoListAdapter.removeRecord(record)
                }
            }
            .addOnFailureListener {
                toast("failed to delete record")
            }
        binding.recordsLoader.visibility = View.GONE

    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}