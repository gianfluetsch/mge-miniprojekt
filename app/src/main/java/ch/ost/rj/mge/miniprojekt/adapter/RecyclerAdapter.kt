package ch.ost.rj.mge.miniprojekt.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.Item
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.model_item.view.*

class RecyclerAdapter(private val listener: OnItemClickListener, private val cellClickListener: CellClickListener) :
    RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private var items = emptyList<Item>()
    lateinit var inflater: LayoutInflater


    // Called by RecyclerView, when new ViewHolder is created, Parent = RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // LayoutInflater turns xml in Layout-Object
        inflater = LayoutInflater.from(parent.context)
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.model_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        holder.textViewTitle.text = currentItem.name
        val date = currentItem.date
        val dateFormatted = date.split("-")
        val year = dateFormatted[0]
        val month = dateFormatted[1]
        val day = dateFormatted[2]
        holder.dateViewTitle.text = "$day.$month.$year"

        if (currentItem.picture == "") {
            holder.imageView.setImageResource(R.drawable.ic_baseline_android_64)
        } else {
            Glide.with(inflater.context).load(Uri.parse(currentItem.picture)).circleCrop()
                .into(holder.imageView)
        }

        if (currentItem.favorite == 0) {
            holder.buttonFav.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
        } else {
            holder.buttonFav.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
        }
        holder.buttonFav.setOnClickListener { cellClickListener.onCellClickListener(currentItem, holder.buttonFav) }

    }

    internal fun setItems(items: List<Item>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val imageView: ImageView = itemView.image_view
        val textViewTitle: TextView = itemView.text_title
        val dateViewTitle: TextView = itemView.date_title
        val buttonFav: Button = itemView.button_favorite

        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(v: View?) {
            val position: Int = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(items[position], position)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: Item, position: Int)
    }

    interface CellClickListener {
        fun onCellClickListener(item: Item, button: Button)
    }

}