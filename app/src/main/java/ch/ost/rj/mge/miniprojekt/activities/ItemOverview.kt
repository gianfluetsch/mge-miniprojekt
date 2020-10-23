package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.InputType
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
    private lateinit var buttonDelete : Button
    private lateinit var itemViewModel: InventoryViewModel
    private lateinit var buttonEdit: FloatingActionButton

    private lateinit var item: String
    private lateinit var description: String
    private lateinit var picture: String
    private lateinit var date: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_item)

        itemTitle = findViewById<TextView>(R.id.tv_toolbar_custom)
        itemDescription = findViewById<TextView>(R.id.item_description_info)
        itemPicture = findViewById<ImageView>(R.id.imageView)
        buttonDelete = findViewById<Button>(R.id.button_delete)
        buttonEdit = findViewById<FloatingActionButton>(R.id.button_modify)
        itemDate = findViewById<TextView>(R.id.item_date_info)

        val intent = intent
        item = intent.getStringExtra(Overview.ITEM)!!
        description = intent.getStringExtra(Overview.DESCRIPTION)!!
        picture = intent.getStringExtra(Overview.PICTURE)!!
        date = intent.getStringExtra(Overview.DATE)!!

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
            builder.setMessage("Do you really want to delete item $item").setTitle("Delete Item $item")
            builder.setPositiveButton("YES") {dialog, id ->
                val item = Item(item, description, picture, date)
                itemViewModel.deleteItem(item)
                finish()
            }
            builder.setNegativeButton("NO") {dialog, id ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()

        }

        buttonEdit.setOnClickListener {
            val intent = Intent(this, CreateItem::class.java)
            intent.putExtra(Overview.ITEM, item)
            intent.putExtra(Overview.DESCRIPTION, description)
            intent.putExtra(Overview.PICTURE, picture)
            intent.putExtra(Overview.DATE, date)
            intent.putExtra(Overview.MODIFY, true)
            startActivity(intent)
            finish()
        }

    }
}