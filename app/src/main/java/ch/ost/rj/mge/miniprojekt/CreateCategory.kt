package ch.ost.rj.mge.miniprojekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CreateCategory  : AppCompatActivity() {
    val NEW_CATEGORY = "category"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        val btnSaveCategory = findViewById<Button>(R.id.add_category_button_save)

        btnSaveCategory.setOnClickListener {
           switchToOverview()
        }

    }

    fun switchToOverview() {
        val editViewName = findViewById<EditText>(R.id.category_name_edit)

        if (editViewName.text.toString().trim().isNullOrBlank()) {
            Toast.makeText(this, "Please insert Category Name", Toast.LENGTH_SHORT).show()

        } else {
            val resultIntent = Intent(this, MainActivity::class.java)
            resultIntent.putExtra(NEW_CATEGORY, "${editViewName.text.toString()}")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }


}