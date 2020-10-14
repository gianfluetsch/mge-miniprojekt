package ch.ost.rj.mge.miniprojekt.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.ost.rj.mge.miniprojekt.model.Item
import ch.ost.rj.mge.miniprojekt.R
import org.w3c.dom.Text

class CategoryOverview : AppCompatActivity() {
    val CATEGORY = "category"

    private var list = ArrayList<Item>()
    private lateinit var categoryTitle: TextView
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_category)
        categoryTitle = findViewById<TextView>(R.id.tv_toolbar_custom)
        val intent = intent
        val category = intent.getStringExtra(CATEGORY)
        if (category != null) {
            logStateChange(category)
        }
        categoryTitle.text = category

        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)
        checkIfEmptyRecyclerView(emptyView, emptyImageView)

    }

    private fun checkIfEmptyRecyclerView(emptyView: View, emptyImageView: ImageView) {
        if (list.isEmpty()) {
            emptyView.visibility = View.VISIBLE
            emptyImageView.visibility = View.VISIBLE
        } else {
            emptyView.visibility = View.GONE
            emptyImageView.visibility = View.GONE
        }
    }


    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }
}