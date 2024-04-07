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

    /**
     * Nosso Adapter irá criar alguns view holders para exibir os dados dos itens em nossa lista.
     * Para cada view que ele criar na tela em formato de lista ele passará aqui.
     * Precisamos informar para ele qual é o layout que precisa ser criado na tela.
     */
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.rv_item, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * onBindViewHolder será chamado sempre que nosso RecyclerView.Adapter estiver "carregando dados"
     * de nossa lista para uma posição do Recycler View que está na tela.
     * Lembrando do conceito de Recycler View, ele reaproveita as views na tela, apenas carregando
     * o conteúdo da view novamente, sem precisar recarregar toda a view.
     *
     * Sempre que é disparado ele fornece um position: Int para sabermos qual item está sendo carregado.
     * Também fornece o ViewHolder que conhece as referências das views para preenchermos com dados ou
     * ler informações, disparos de clicks, etc.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Pegamos o item em nossa lista que deve ser "montado" nessa view
        val toDoItem: ToDoItem = dataSet[position]

        // Preenchemos os dados desse item na tela
        holder.tvTitle.text = toDoItem.title
        holder.tvDescription.text = toDoItem.description
        holder.cbCompleted.isChecked = toDoItem.completed

        holder.itemView.setOnClickListener {
            itemOnClick(position, toDoItem)
        }

        holder.cbCompleted.setOnCheckedChangeListener(null)

        holder.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
            // Atualiza o estado do item na lista
            toDoItem.completed = isChecked
            // Atualiza o estado do item no banco de dados
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

    /**
     * RecyclerView.ViewHolder responsável por "guardar" as referências das views
     * de cada item de nossa lista. Assim podemos usar essas referências para
     * preencher com dados quando a lista está sendo carregada.
     *
     * Aqui devemos usar findViewById para referenciar os itens em rv_item.xml
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tv_title)
        val tvDescription: TextView = view.findViewById(R.id.tv_description)
        val cbCompleted: CheckBox = view.findViewById(R.id.cb_completed)
    }
}