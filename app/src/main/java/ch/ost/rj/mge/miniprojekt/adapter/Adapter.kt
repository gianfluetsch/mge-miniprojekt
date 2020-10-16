package ch.ost.rj.mge.miniprojekt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.storage.Category

class Adapter internal constructor(
    context: Context
) : RecyclerView.Adapter<Adapter.WordViewHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var categorys = emptyList<Category>() // Cached copy of words

    inner class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val inventoryItemView: TextView = itemView.findViewById(R.id.text_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val itemView = inflater.inflate(R.layout.model_category, parent, false)
        return WordViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val current = categorys[position]
        holder.inventoryItemView.text = current.name
    }

    internal fun setCategorys(categorys: List<Category>) {
        this.categorys = categorys
        notifyDataSetChanged()
    }

    override fun getItemCount() = categorys.size
}


