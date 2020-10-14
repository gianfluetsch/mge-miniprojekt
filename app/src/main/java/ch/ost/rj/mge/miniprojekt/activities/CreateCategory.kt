package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import ch.ost.rj.mge.miniprojekt.R

class CreateCategory : AppCompatActivity() {
    val NEW_CATEGORY = "category"

    private lateinit var btnSaveCategory: Button
    private lateinit var categoryNameInput: String
    private lateinit var editViewName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        validateInput()

        btnSaveCategory.setOnClickListener {
            val resultIntent = Intent(this, Overview::class.java)
            resultIntent.putExtra(NEW_CATEGORY, "$categoryNameInput")
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun validateInput() {
        editViewName = findViewById<EditText>(R.id.category_name_edit)
        btnSaveCategory = findViewById<Button>(R.id.add_category_button_save)

        editViewName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                categoryNameInput = editViewName.text.toString().trim()
                btnSaveCategory.isEnabled = !categoryNameInput.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

}