package ch.ost.rj.mge.miniprojekt

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category.view.*

class Adapter(private val list: List<Item>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    // Parent = RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.category, parent, false);
        return ViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = list[position];

        holder.imageView.setImageResource(currentItem.imageRessource);
        holder.textViewTitle.text = currentItem.title;
    }

    override fun getItemCount(): Int {
        return list.size;
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.image_view;
        val textViewTitle: TextView = itemView.text_title;

        // Construktor body in Java
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = adapterPosition;
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position);
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int);
    }
}