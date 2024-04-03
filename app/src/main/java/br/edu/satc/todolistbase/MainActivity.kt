package br.edu.satc.todolistbase

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import br.edu.satc.todolistbase.data.ToDoItemAdapter
import br.edu.satc.todolistbase.roomdatabase.AppDatabase
import br.edu.satc.todolistbase.roomdatabase.ToDoItem
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var TAG = "MainActivity"
    private lateinit var db: AppDatabase
    private lateinit var toDoItemList: ArrayList<ToDoItem>
    private lateinit var toDoItemAdapter: ToDoItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initDatabase()

        initRecyclerViewAdapter()

        loadData()

        // Pega a referência de nosso botão FloatActionButton e seu click
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {

            val it = Intent(this, NewItemActivity::class.java)
            startActivity(it)

            // Inclusao fake. Add um item na lista
            // cria o item
            //val item = ToDoItem(
            //    toDoItemList.size,
            //    "Item de teste ${toDoItemList.size + 1}"
            //)
            // add o item na lista
            //toDoItemList.add(item)
            // informa o adapter que houve uma atualizacao na lista para ele refletir isso em tela
            //toDoItemAdapter.notifyItemChanged(toDoItemList.size-1)
            // Salva no banco de dados
            //db.toDoItemDao().insertAll(item)
        }
    }

    /**
     * Inicializa o banco de dados.
     * Instancia AppDatabase passando o nome do banco que queremos abrir.
     * É importante lembrarmos que abrir e fechar um banco de dados é algo custoso.
     * Portanto deixamos esse banco aberto enquanto queremos usar para insert, update, read, delete.
     *
     * Com o database iniciado temos acesso a interface DAO que disponibilza os metodos de
     * interação com o banco de dados para ler, inserir ou atualizar dados.
     */
    private fun initDatabase() {
        // Inicializar nosso banco de dados Android Room Persistence com SQLITE
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-todolist"
        )
            .allowMainThreadQueries()
            .build()

        // Utilizar a interface DAO para interagir com o database
//        db.toDoItemDao().getAll()
//        db.toDoItemDao().getToDoItem(itemId)
//        db.toDoItemDao().insertAll(toDoItem)

    }

    /**
     * Aqui iniciamos a nossa lista de itens que irá aparecer em tela.
     * Pegamos a referência da RecyclerView em nosso arquivo de layout
     * Criamos o adapter
     * Inicializamos a lista de items (ArrayList)
     * Atribuímos a lista ao adapter e o adapter ao nosso RecyclerView
     */
    private fun initRecyclerViewAdapter() {

        // Pegar a referência de nossa RecyclerView e setar seu gerenciador de layout
        val rv = findViewById<RecyclerView>(R.id.rv_itens)
        rv.layoutManager = LinearLayoutManager(this)

        // Cria nossa lista de itens que futuramente irá receber a lista de itens do banco de dados
        toDoItemList = ArrayList()

        // Prepara nosso método de click em um item da lista
        val itemOnClick: (Int, ToDoItem) -> Unit = { position, item ->
            Log.d(TAG, "Click pos: $position | desc: ${item.description}")
        }

        // Instancia o adapter passando a lista e o método que será disparado no click de item
        toDoItemAdapter = ToDoItemAdapter(toDoItemList, itemOnClick)

        // Informa nosso recycler view qual adapter irá cuidar de seus dados
        rv.adapter = toDoItemAdapter

    }

    private fun loadData() {
        toDoItemList.addAll(db.toDoItemDao().getAll() as ArrayList<ToDoItem>)
        toDoItemAdapter.notifyDataSetChanged()
    }
}