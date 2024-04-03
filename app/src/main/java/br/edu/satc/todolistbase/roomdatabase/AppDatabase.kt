package br.edu.satc.todolistbase.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * O código abaixo define uma classe AppDatabase para armazenar o banco de dados.
 * A classe AppDatabase define a configuração do banco de dados e serve como o ponto de acesso
 * principal do app aos dados persistidos.
 */
@Database(entities = [ToDoItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun toDoItemDao(): ToDoItemDao
}
