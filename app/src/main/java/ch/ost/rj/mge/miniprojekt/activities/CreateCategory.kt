package ch.ost.rj.mge.miniprojekt.activities

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.Category
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import com.google.android.material.snackbar.Snackbar

class CreateCategory : AppCompatActivity() {

    private lateinit var btnSaveCategory: Button
    private lateinit var categoryNameInput: String
    private lateinit var descriptionInput: String
    private lateinit var editViewName: EditText
    private lateinit var editDescription: EditText
    private lateinit var imageView: ImageView
    private lateinit var btnImage: Button
    private lateinit var imageUri: String
    private lateinit var catviewModel: InventoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_category)

        validateInput()

        catviewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

        btnSaveCategory.setOnClickListener {
            var message : String = ""
            catviewModel.checkItemExist(categoryNameInput).observeOnce(this, Observer { categorys ->
                categorys?.let {
                    if (it.toInt() == 0) {
                        message = "Item $categoryNameInput added"
                        val category = Category(categoryNameInput, descriptionInput, imageUri)
                        catviewModel.insert(category)
                        waitForObserver(message)
                    } else {
                        message = "Item $categoryNameInput exists already"
                        waitForObserver(message)
                    }
                }
            })
        }
    }

    fun waitForObserver(message: String) {
        val resultIntent = Intent(this, Overview::class.java)
        resultIntent.putExtra(Overview.CATEGORY, categoryNameInput)
        resultIntent.putExtra(Overview.MESSAGE, message)
        setResult(RESULT_OK, resultIntent)

        finish()
    }

    fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
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
            showImageOptionDialog()
        }
    }

    private fun getImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, Overview.GALLERY_REQUEST)
    }

    private fun capturePictureFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, Overview.CAMERA_REQUEST)
    }

    private fun showImageOptionDialog() {
        val options = resources.getStringArray(R.array.image_options)
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.select_image_dialog)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> getImageFromGallery()
                    1 -> capturePictureFromCamera()
                }
            }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && requestCode == Overview.GALLERY_REQUEST) {
            imageView.setImageURI(data?.data)
            val uri: Uri? = data?.data
            contentResolver.takePersistableUriPermission(uri!!, Intent.FLAG_GRANT_READ_URI_PERMISSION)
            imageUri = uri.toString()

        } else if (resultCode == RESULT_OK && requestCode == Overview.CAMERA_REQUEST) {
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
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