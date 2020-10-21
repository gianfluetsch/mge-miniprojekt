package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.adapter.RecyclerAdapter
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import ch.ost.rj.mge.miniprojekt.model.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_overview.*

class Overview : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {

    companion object {
    const val DESCRIPTION = "description"
    const val PICTURE = "picture"
    const val RESPONSE_CATEGORY = 0
    const val CALLBACK_CODE = 1
    const val CATEGORY = "category"
    const val MESSAGE = "message"
    const val GALLERY_REQUEST = 7
    const val CAMERA_REQUEST = 8
    var darkMode = false
    var size = 0
    }

    val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE

    private val recyclerAdapter = RecyclerAdapter(this)
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView
    private lateinit var btnAddCategory: FloatingActionButton
    private lateinit var catviewModel: InventoryViewModel
    private lateinit var category: Category


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(findViewById(R.id.toolbar))
        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)

        catviewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)

        catviewModel.allCategorys.observe(this, Observer { categorys ->
            categorys?.let { recyclerAdapter.setCategorys(it) }
        })

        btnAddCategory = findViewById<FloatingActionButton>(R.id.add_category_button)
        btnAddCategory.setOnClickListener {
            val intent = Intent(this, CreateCategory::class.java)
            startActivityForResult(intent, RESPONSE_CATEGORY)
        }

        updateRecyclerView()
        getDBSize()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun updateRecyclerView() {
        recylerView.adapter = recyclerAdapter
        // Zuständig für Positionierung innerhalb RecyclerView
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)

//        getDBSize()
    }

    private fun getDBSize() {
        // warten bis observer ausgeführt -> erst dann size benutzen
        catviewModel.checkDB.observe(this, Observer { categorys ->
            categorys?.let { waitForObserver(it) }
        })
    }

    private fun waitForObserver(dbSize: Integer) {
        size = dbSize.toInt()
        checkIfEmptyRecyclerView()
    }

    private fun checkIfEmptyRecyclerView() {
        if (size == 0) {
            recylerView.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
            emptyImageView.visibility = View.VISIBLE
        } else {
            recylerView.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
            emptyImageView.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESPONSE_CATEGORY && resultCode == RESULT_OK) {
            val message = data?.getStringExtra(MESSAGE)

            data?.getStringExtra(CATEGORY)?.let {
                if (message != null) {
                    createSnackBar(rootLayout, message)
                }
                Unit
            }
        }
                getDBSize()
    }

    private fun createSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("Hide") { snackBar.dismiss() }.show()
    }

    override fun onItemClick(category: Category, position: Int) {
        this.category = category
//        setupPermissions()
//        makeRequest()
        loadContentFromRoom()
    }

    private fun loadContentFromRoom() {
        val intent = Intent(this, CategoryOverview::class.java)
        val itemCategory = category.name
        val itemDescription = category.description
        val picture = category.picture
        intent.putExtra(CATEGORY, itemCategory)
        intent.putExtra(DESCRIPTION, itemDescription)
        intent.putExtra(PICTURE, picture)
        startActivityForResult(intent, RESPONSE_CATEGORY)
    }

    private fun setupPermissions() {
        val status = ContextCompat.checkSelfPermission(this, permission)

        if (status != PackageManager.PERMISSION_GRANTED) {
            logStateChange("Permission for Gallery required")
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Permission to access external Storage is required")
                    .setTitle("Permission required")
                builder.setPositiveButton("OK") { dialog, id ->
                    logStateChange("ok clicked")
                    makeRequest()
                }
                val dialog = builder.create()
                dialog.show()
            } else {
//                makeRequest()
            }
        }
        makeRequest()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(permission), CALLBACK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CALLBACK_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    logStateChange("Permission denied")
                } else {
                    logStateChange("Permission granted")
                    loadContentFromRoom()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                if (darkMode) {
                    darkMode = false
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    delegate.applyDayNight()
                } else {
                    darkMode = true
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    delegate.applyDayNight()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu!!.findItem(R.id.action_settings)
        if (darkMode) {
            item.title = "Light Mode"
        } else {
            item.title = "Dark Mode"
        }

        return super.onPrepareOptionsMenu(menu)
    }

    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }
}