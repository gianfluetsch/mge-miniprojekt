package ch.ost.rj.mge.miniprojekt.activities

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ch.ost.rj.mge.miniprojekt.model.Item
import ch.ost.rj.mge.miniprojekt.R

class CategoryOverview : AppCompatActivity() {
    val CATEGORY = "category"
    private var list = ArrayList<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview_category)
        val categoryTitle = findViewById<TextView>(R.id.tv_toolbar_custom)
        val intent = intent
        val category = intent.getStringExtra(CATEGORY)
        if (category != null) {
            logStateChange(category)
        }
        categoryTitle.text = category

        val emptyView = findViewById<TextView>(R.id.empty_view)
        val emptyImageView = findViewById<ImageView>(R.id.empty_imageView)
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