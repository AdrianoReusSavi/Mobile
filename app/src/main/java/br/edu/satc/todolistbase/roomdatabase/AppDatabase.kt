package br.edu.satc.todolistbase.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ToDoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao
}