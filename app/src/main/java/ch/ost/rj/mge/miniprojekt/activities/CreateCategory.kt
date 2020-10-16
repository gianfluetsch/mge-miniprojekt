package ch.ost.rj.mge.miniprojekt.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import ch.ost.rj.mge.miniprojekt.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_add_category.view.*
import java.util.jar.Manifest

class CreateCategory : AppCompatActivity() {
    val NEW_CATEGORY = "category"
    val DESCRIPTION = "description"
    val PICTURE = "picture"

    private lateinit var btnSaveCategory: Button
    private lateinit var categoryNameInput: String
    private lateinit var descriptionInput: String
    private lateinit var editViewName: EditText
    private lateinit var editDescription: EditText
    private lateinit var imageView: ImageView
    private lateinit var btnImage: Button
    private lateinit var imageUri: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        validateInput()

        btnSaveCategory.setOnClickListener {
            val resultIntent = Intent(this, Overview::class.java)
            resultIntent.putExtra(NEW_CATEGORY, categoryNameInput)
            resultIntent.putExtra(DESCRIPTION, descriptionInput)
            resultIntent.putExtra(PICTURE, imageUri)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun validateInput() {
        editViewName = findViewById<EditText>(R.id.category_name_edit)
        btnSaveCategory = findViewById<Button>(R.id.add_category_button_save)
        editDescription = findViewById<EditText>(R.id.item_description_edit)
        imageView = findViewById<ImageView>(R.id.imageView)
        btnImage = findViewById<Button>(R.id.add_image)

        editViewName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                categoryNameInput = editViewName.text.toString().trim()
                btnSaveCategory.isEnabled = !categoryNameInput.isNullOrBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                descriptionInput = editDescription.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            imageView.setImageURI(data?.data)
            val uri: Uri? = data?.data
            imageUri = uri.toString()
            logStateChange("$uri")
        }

    }

    private fun createSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("Hide") { snackBar.dismiss() }.show()
    }

    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }

}