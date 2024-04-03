package br.edu.satc.todolistbase.roomdatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ToDoItemDao {

    /**
     * Esta interface define os métodos de comunicação com o banco de dados usando SQL
     * Crie métodos de insert, update e delete conforme as regras de negócio da App
     *
     * Monte a lógica SQL usando a declaração @Query e utilize parâmetros conforme os exemplos
     */

    @Query("SELECT * FROM todoitens WHERE uid = :uid")
    fun getToDoItem(uid: String): ToDoItem

    @Query("SELECT * FROM todoitens")
    fun getAll(): List<ToDoItem>

    @Insert
    fun insertAll(vararg items: ToDoItem)

    @Delete
    fun delete(item: ToDoItem)
}