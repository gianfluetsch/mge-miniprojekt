package ch.ost.rj.mge.miniprojekt.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.Category
import kotlinx.android.synthetic.main.model_category.view.*

class RecyclerAdapter(private val listener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var categorys = emptyList<Category>()

    // Called by RecyclerView, when new ViewHolder is created, Parent = RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       // LayoutInflater turns xml in Layout-Object
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.model_category, parent, false)
        return ViewHolder(itemView)
    }

    // wenn gescrollt, geupadted, etc wird
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = categorys[position]

//        holder.imageView.setImageResource(currentItem.imageRessource)
        holder.textViewTitle.text = currentItem.name
    }

    internal fun setCategorys(categorys: List<Category>) {
        this.categorys = categorys
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return categorys.size
    }

    // ViewHolder represents row in RecyclerView-List
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.image_view
        val textViewTitle: TextView = itemView.text_title

        // Coming....Bei Click on Image
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(categorys[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(category: Category, position: Int)
    }

}