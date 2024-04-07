package br.edu.satc.todolistbase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.satc.todolistbase.data.ToDoItemAdapter
import br.edu.satc.todolistbase.roomdatabase.AppDatabase
import br.edu.satc.todolistbase.roomdatabase.ToDoItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var toDoItemList: ArrayList<ToDoItem>
    private lateinit var toDoItemAdapter: ToDoItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatabase()

        initRecyclerViewAdapter()

        loadData()

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val intent = Intent(this, NewItemActivity::class.java)
            startActivityForResult(intent, NEW_ITEM_REQUEST_CODE)
        }
    }

    private fun initDatabase() {
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database-todolist")
            .allowMainThreadQueries()
            .build()
    }

    private fun initRecyclerViewAdapter() {
        val rv = findViewById<RecyclerView>(R.id.rv_itens)
        rv.layoutManager = LinearLayoutManager(this)
        toDoItemList = ArrayList()
        toDoItemAdapter = ToDoItemAdapter(toDoItemList, ::onItemClick, db)
        rv.adapter = toDoItemAdapter
    }

    private fun loadData() {
        toDoItemList.clear()
        toDoItemList.addAll(db.toDoItemDao().getAll())
        toDoItemAdapter.notifyDataSetChanged()
    }

    private fun onItemClick(position: Int, item: ToDoItem) {
        item.completed = !item.completed
        db.toDoItemDao().update(item)
        toDoItemAdapter.notifyItemChanged(position)
    }

    companion object {
        private const val NEW_ITEM_REQUEST_CODE = 1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NEW_ITEM_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            loadData()
        }
    }
}