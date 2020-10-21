package ch.ost.rj.mge.miniprojekt.activities

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.Category
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel

class CategoryOverview : AppCompatActivity() {
    private lateinit var categoryTitle: TextView
    private lateinit var categoryDescription: TextView
    private lateinit var categoryPicture: ImageView
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView
    private lateinit var buttonDelete : Button
    private lateinit var catviewModel: InventoryViewModel

    private lateinit var category: String
    private lateinit var description: String
    private lateinit var picture: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_category)

        categoryTitle = findViewById<TextView>(R.id.tv_toolbar_custom)
        categoryDescription = findViewById<TextView>(R.id.item_description_info)
        categoryDescription.inputType = InputType.TYPE_NULL
        categoryPicture = findViewById<ImageView>(R.id.imageView)
        buttonDelete = findViewById<Button>(R.id.button_delete)

        val intent = intent
        category = intent.getStringExtra(Overview.CATEGORY)!!
        description = intent.getStringExtra(Overview.DESCRIPTION)!!
        picture = intent.getStringExtra(Overview.PICTURE)!!

        categoryTitle.text = category
        categoryDescription.text = description

        categoryPicture.setImageURI(Uri.parse(picture))

        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)

        catviewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

        buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you really want to delete item $category").setTitle("Delete Item $category")
            builder.setPositiveButton("YES") {dialog, id ->
                val category = Category(category, description, picture)
                catviewModel.deleteCategory(category)
                finish()
            }
            builder.setNegativeButton("NO") {dialog, id ->
                dialog.cancel()
            }
            val dialog = builder.create()
            dialog.show()

        }

    }
}