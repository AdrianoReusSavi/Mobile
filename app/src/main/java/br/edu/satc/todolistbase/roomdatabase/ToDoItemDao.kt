package br.edu.satc.todolistbase.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ToDoItemDao {
    @Query("SELECT * FROM todoitens WHERE uid = :uid")
    fun getToDoItem(uid: Int): ToDoItem

    @Query("SELECT * FROM todoitens")
    fun getAll(): List<ToDoItem>

    @Insert
    fun insertAll(vararg items: ToDoItem)

    @Delete
    fun delete(item: ToDoItem)

    @Update
    fun update(item: ToDoItem)
}