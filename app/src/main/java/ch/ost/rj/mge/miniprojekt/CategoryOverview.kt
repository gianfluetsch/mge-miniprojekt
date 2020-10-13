package ch.ost.rj.mge.miniprojekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CategoryOverview : AppCompatActivity() {
    val CATEGORY = "category"

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

    }


    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }
}