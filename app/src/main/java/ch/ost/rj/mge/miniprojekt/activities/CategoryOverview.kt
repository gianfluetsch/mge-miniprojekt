package ch.ost.rj.mge.miniprojekt.activities

import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.ost.rj.mge.miniprojekt.R

class CategoryOverview : AppCompatActivity() {
    val DESCRIPTION = "description"
    val CATEGORY = "category"
    val PICTURE = "picture"

    private lateinit var categoryTitle: TextView
    private lateinit var categoryDescription: TextView
    private lateinit var categoryPicture: ImageView
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView

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

        val intent = intent
        category = intent.getStringExtra(CATEGORY)!!
        description = intent.getStringExtra(DESCRIPTION)!!
        picture = intent.getStringExtra(PICTURE)!!

        categoryTitle.text = category
        categoryDescription.text = description
        categoryPicture.setImageURI(Uri.parse(picture))

        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)

    }
}