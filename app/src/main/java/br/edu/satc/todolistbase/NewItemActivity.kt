package br.edu.satc.todolistbase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import br.edu.satc.todolistbase.roomdatabase.AppDatabase
import br.edu.satc.todolistbase.roomdatabase.ToDoItem

class NewItemActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var btSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_item)

        etTitle = findViewById(R.id.et_title)
        etDescription = findViewById(R.id.et_description)
        btSave = findViewById(R.id.bt_save)

        db = AppDatabase.getInstance(applicationContext)

        btSave.setOnClickListener { save() }
    }

    private fun save() {
        val newItem = ToDoItem(
            title = etTitle.toString(),
            description = etDescription.toString(),
            completed = false
        )

        db.toDoItemDao().insertAll(newItem)
        finish()
    }
}