package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.adapter.RecyclerAdapter
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import ch.ost.rj.mge.miniprojekt.model.storage.Category
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_overview.*

class Overview : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {
    private val RESPONSE_CATEGORY = 0
    val CALLBACK_CODE = 1
    val permission = android.Manifest.permission.READ_EXTERNAL_STORAGE
    private val NEW_CATEGORY = "category"
    val DESCRIPTION = "description"
    val PICTURE = "picture"

    private val recyclerAdapter = RecyclerAdapter(this)
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView
    private lateinit var btnAddCategory: FloatingActionButton
    private lateinit var catviewModel: InventoryViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(findViewById(R.id.toolbar))

        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)
//        checkIfEmptyRecyclerView()

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
    }


    private fun updateRecyclerView() {
        recylerView.adapter = recyclerAdapter
        // Zuständig für Positionierung innerhalb RecyclerView
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)
    }

    private fun checkIfEmptyRecyclerView() {
        logStateChange("${recyclerAdapter.itemCount}")
        if (recyclerAdapter.itemCount == 0) {
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
            val result = data?.getStringExtra(NEW_CATEGORY)
            val description = data?.getStringExtra(DESCRIPTION)
            val uri = data?.getStringExtra(PICTURE)

            data?.getStringExtra(NEW_CATEGORY)?.let {
                val category = Category(it, description, uri)
                catviewModel.insert(category)
                createSnackBar(rootLayout, "Category $result added")
                Unit
            }
        } else {
            Toast.makeText(
                applicationContext,
                "not safed",
                Toast.LENGTH_LONG
            ).show()
        }
//                checkIfEmptyRecyclerView()
    }


    private fun createSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("Hide") { snackBar.dismiss() }.show()
    }


    override fun onItemClick(category: Category, position: Int) {
        setupPermissions()
        val intent = Intent(this, CategoryOverview::class.java)
        val itemCategory = category.name
        val itemDescription = category.description
        val picture = category.picture
        intent.putExtra(NEW_CATEGORY, itemCategory)
        intent.putExtra(DESCRIPTION, itemDescription)
        intent.putExtra(PICTURE, picture)
        startActivityForResult(intent, RESPONSE_CATEGORY)
    }

    private fun setupPermissions() {
        val status = ContextCompat.checkSelfPermission(this, permission)

        if (status != PackageManager.PERMISSION_GRANTED) {
            createSnackBar(add_category_layout, "Permission for Gallery required")
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(permission), CALLBACK_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != CALLBACK_CODE)
            return;
        if (grantResults.isEmpty())
            return;
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            createSnackBar(add_category_layout, "Permission granted")
        } else {
            createSnackBar(add_category_layout, "Permission denied")
        }
    }


    // Allenfalls für Darkmode etc. nötig !!!
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }
}