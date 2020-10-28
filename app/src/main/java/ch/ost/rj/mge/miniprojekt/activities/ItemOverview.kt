package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import ch.ost.rj.mge.miniprojekt.model.Item
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemOverview : AppCompatActivity() {
    private lateinit var itemTitle: TextView
    private lateinit var itemDescription: TextView
    private lateinit var itemPicture: ImageView
    private lateinit var itemDate: TextView
    private lateinit var buttonDelete: Button
    private lateinit var buttonFavorite: Button
    private lateinit var itemViewModel: InventoryViewModel
    private lateinit var buttonEdit: FloatingActionButton
    private lateinit var item: String
    private lateinit var description: String
    private lateinit var picture: String
    private lateinit var date: String
    private var favorite = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_item)

        itemTitle = findViewById(R.id.tv_toolbar_custom)
        itemDescription = findViewById(R.id.item_description_info)
        itemPicture = findViewById(R.id.imageView)
        buttonDelete = findViewById(R.id.button_delete)
        buttonEdit = findViewById(R.id.button_modify)
        itemDate = findViewById(R.id.item_date_info)
        buttonFavorite = findViewById(R.id.button_favorite_item)

        val intent = intent
        item = intent.getStringExtra(Overview.ITEM)!!
        description = intent.getStringExtra(Overview.DESCRIPTION)!!
        picture = intent.getStringExtra(Overview.PICTURE)!!
        date = intent.getStringExtra(Overview.DATE)!!
        favorite = intent.getIntExtra(Overview.FAVORITE, 0)!!

        if (favorite == 0) {
            buttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
        } else {
            buttonFavorite.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
        }

        if (picture == "") {
            itemPicture.setImageResource(R.drawable.ic_baseline_android_64)
        } else {
            itemPicture.setImageURI(Uri.parse(picture))
        }

        itemTitle.text = item
        itemDescription.text = description
        val dateFormatted = date.split("-")
        val year = dateFormatted[0]
        val month = dateFormatted[1]
        val day = dateFormatted[2]
        itemDate.text = "$day.$month.$year"

        itemViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

        buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you really want to delete item $item")
                .setTitle("Delete Item $item")
            builder.setPositiveButton("YES") { _, _ ->
                val item = Item(item, description, picture, date, 0)
                itemViewModel.deleteItem(item)
                finish()
            }
            builder.setNegativeButton("NO") { dialog, _ ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()
        }

        buttonEdit.setOnClickListener {
            val intentNewItem = Intent(this, CreateItem::class.java)
            intentNewItem.putExtra(Overview.ITEM, item)
            intentNewItem.putExtra(Overview.DESCRIPTION, description)
            intentNewItem.putExtra(Overview.PICTURE, picture)
            intentNewItem.putExtra(Overview.DATE, date)
            intentNewItem.putExtra(Overview.MODIFY, true)
            intentNewItem.putExtra(Overview.FAVORITE, favorite)
            startActivity(intentNewItem)
            finish()
        }
    }
}