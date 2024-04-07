package br.edu.satc.todolistbase.data

import android.graphics.Paint
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.edu.satc.todolistbase.R
import br.edu.satc.todolistbase.roomdatabase.AppDatabase
import br.edu.satc.todolistbase.roomdatabase.ToDoItem

class ToDoItemAdapter (
    private val dataSet: ArrayList<ToDoItem>,
    private val itemOnClick: (Int, ToDoItem) -> Unit,
    private val db: AppDatabase
) : RecyclerView.Adapter<ToDoItemAdapter.ViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val toDoItem: ToDoItem = dataSet[position]

        holder.tvTitle.text = toDoItem.title
        holder.tvDescription.text = toDoItem.description
        holder.cbCompleted.isChecked = toDoItem.completed

        holder.itemView.setOnClickListener {
            itemOnClick(position, toDoItem)
        }

        holder.cbCompleted.setOnCheckedChangeListener(null)

        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            toDoItem.completed = isChecked
            updateItemInDatabase(toDoItem)
        }

        val strikeThroughFlag = Paint.STRIKE_THRU_TEXT_FLAG

        if (toDoItem.completed) {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags or strikeThroughFlag
            holder.tvDescription.paintFlags = holder.tvDescription.paintFlags or strikeThroughFlag
        } else {
            holder.tvTitle.paintFlags = holder.tvTitle.paintFlags and strikeThroughFlag.inv()
            holder.tvDescription.paintFlags = holder.tvDescription.paintFlags and strikeThroughFlag.inv()
        }
    }

    private fun updateItemInDatabase(toDoItem: ToDoItem) {
        db.toDoItemDao().update(toDoItem)
        val handler = Handler(Looper.getMainLooper())
        handler.post(::notifyDataSetChanged)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val cbCompleted: CheckBox = view.findViewById(R.id.cb_completed)
    }
}