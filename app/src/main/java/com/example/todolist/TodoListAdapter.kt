package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.RecordItemBinding

class TodoListAdapter(
    private var records: ArrayList<RecordDocument>,
    private val listener: RecordClickListener
) :
    RecyclerView.Adapter<TodoListAdapter.ListSelectionViewHolder>() {

    interface RecordClickListener {
        fun itemClicked(record: RecordDocument)
    }

    class ListSelectionViewHolder(
        private val binding: RecordItemBinding,
        private val listener: RecordClickListener
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(record: RecordDocument) {
            binding.recordItem.text = record.record

            binding.deleteRecord.setOnClickListener {
                listener.itemClicked(record)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListSelectionViewHolder {
        val binding = RecordItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListSelectionViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ListSelectionViewHolder, position: Int) {
        holder.onBind(records[position])
    }

    override fun getItemCount(): Int {
        return records.size
    }

    fun removeRecord(record:RecordDocument){
        records.remove(record)
        notifyDataSetChanged()
    }

}