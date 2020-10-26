package ch.ost.rj.mge.miniprojekt.activities

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import ch.ost.rj.mge.miniprojekt.model.Item
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*

class CreateItem : AppCompatActivity() {

    private lateinit var btnSaveItem: Button
    private lateinit var itemTitleInput: String
    private lateinit var itemDescriptionInput: String
    private lateinit var editViewName: EditText
    private lateinit var editDescription: EditText
    private lateinit var textViewToolbar: TextView
    private lateinit var imageView: ImageView
    private lateinit var btnImage: Button
    private lateinit var btnDate: Button
    private lateinit var imageUri: String
    private lateinit var itemViewModel: InventoryViewModel
    private lateinit var itemTitle: String
    private lateinit var itemDescription: String
    private lateinit var itemPicture: String
    private lateinit var itemDate: String
    private var modified = false
    private lateinit var titleOld: String
    private lateinit var textViewDate: TextView
    private val calendar: Calendar = Calendar.getInstance()
    private val year = calendar.get(Calendar.YEAR)
    private val month = calendar.get(Calendar.MONTH)
    private val day = calendar.get(Calendar.DAY_OF_MONTH)
    private var date = "$year-${month + 1}-$day"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_item)

        editViewName = findViewById(R.id.item_name_edit)
        btnSaveItem = findViewById(R.id.add_item_button_save)
        editDescription = findViewById(R.id.item_description_edit)
        imageView = findViewById(R.id.imageView)
        btnImage = findViewById(R.id.add_image)
        btnDate = findViewById(R.id.add_date)
        textViewDate = findViewById(R.id.textview_date)
        textViewToolbar = findViewById(R.id.tv_toolbar_custom)

        checkModified()

        itemViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

    }

    private fun saveItem() {
        btnSaveItem.setOnClickListener {
            var message: String
            val itemNew = Item(itemTitleInput, itemDescriptionInput, imageUri, date)
            if (modified) {
                if (itemTitleInput == titleOld) {
                    itemViewModel.insertReplace(itemNew)
                    waitForObserver("modified")
                } else {
                    val itemOld = Item(titleOld, itemDescription, itemPicture, date)
                    itemViewModel.deleteItem(itemOld)
                    itemViewModel.insert(itemNew)
                    waitForObserver("modified")
                }
            } else {
                itemViewModel.checkItemExist(itemTitleInput)
                    .observeOnce(this, { items ->
                        items?.let {
                            if (it.toInt() == 0) {
                                message = "Item $itemTitleInput added"
                                itemViewModel.insert(itemNew)
                                waitForObserver(message)
                            } else {
                                message = "Item $itemTitleInput exists already"
                                waitForObserver(message)
                            }
                        }
                    })
            }
        }
    }

    private fun checkModified() {
        val intent = intent
        modified = intent.getBooleanExtra(Overview.MODIFY, false)
        if (modified) {
            itemTitle = intent.getStringExtra(Overview.ITEM)!!
            itemTitleInput = itemTitle
            itemDescription = intent.getStringExtra(Overview.DESCRIPTION)!!
            itemDescriptionInput = itemDescription
            itemPicture = intent.getStringExtra(Overview.PICTURE)!!
            imageUri = itemPicture
            itemDate = intent.getStringExtra(Overview.DATE)!!
            date = itemDate
            textViewToolbar.text = "Modify $itemTitle"
            textViewToolbar.text = String.format("Modify ")

            editViewName.text.insert(0, itemTitle)
            editDescription.text.insert(0, itemDescription)
            imageView.setImageURI(Uri.parse(itemPicture))
            textViewDate.text = itemDate

            titleOld = itemTitle
        } else {
            itemDescriptionInput = ""
            imageUri = ""
            textViewDate.text = "$day.${month + 1}.$year"
        }

        validateInput()

    }

    private fun waitForObserver(message: String) {
        val resultIntent = Intent(this, Overview::class.java)
        resultIntent.putExtra(Overview.ITEM, itemTitleInput)
        resultIntent.putExtra(Overview.MESSAGE, message)
        setResult(RESULT_OK, resultIntent)

        finish()
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private fun validateInput() {
        editViewName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemTitleInput = editViewName.text.toString().trim()
                textViewToolbar.text = "Add $itemTitleInput"
                btnSaveItem.isEnabled = !itemTitleInput.isBlank()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        editDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                itemDescriptionInput = editDescription.text.toString()
                if (modified) {
                    btnSaveItem.isEnabled = !itemDescriptionInput.isBlank()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnImage.setOnClickListener {
            showImageOptionDialog()
        }

        btnDate.setOnClickListener {


            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, month, dayOfMonth ->
                    textViewDate.text = "$dayOfMonth.${month + 1}.$year"
                    date = "$year-${month + 1}-$dayOfMonth"
                    if (modified) {
                        btnSaveItem.isEnabled = !date.isBlank()
                    }
                },
                year,
                month,
                day
            )
            datePickerDialog.show()


        }

        saveItem()
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
            contentResolver.takePersistableUriPermission(
                uri!!,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = uri.toString()

        } else if (resultCode == RESULT_OK && requestCode == Overview.CAMERA_REQUEST) {
            val bitmap: Bitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(bitmap)
            val uri: Uri = saveImageToExternalStorage(bitmap)
            imageUri = uri.toString()
        }
        if (modified) {
            btnSaveItem.isEnabled = !imageUri.isBlank()
        }

    }

    private fun saveImageToExternalStorage(bitmap: Bitmap): Uri {
        val fileName = "${UUID.randomUUID()}.jpg"
        var fos: OutputStream? = null
        var imageUriProv : Uri? = null

        contentResolver?.also { resolver ->
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            imageUriProv =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fos = imageUriProv?.let { resolver.openOutputStream(it) }

        }
        fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it) }
        imageUri = imageUriProv.toString()
        return Uri.parse(imageUri)

    }

}