package ch.ost.rj.mge.miniprojekt.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ch.ost.rj.mge.miniprojekt.model.Item
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.adapter.RecyclerAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_overview.*

class Overview : AppCompatActivity(), RecyclerAdapter.OnItemClickListener {
    private val RESPONSE_CATEGORY = 0
    private val NEW_CATEGORY = "category"
    private var list = ArrayList<Item>()
    private var index = 0

    private val recyclerAdapter = RecyclerAdapter(list, this)
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView
    private lateinit var btnAddCategory: FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(findViewById(R.id.toolbar))

        emptyView = findViewById<TextView>(R.id.empty_view)
        emptyImageView = findViewById<ImageView>(R.id.empty_imageView)
        checkIfEmptyRecyclerView(emptyView, emptyImageView)

        btnAddCategory = findViewById<FloatingActionButton>(R.id.add_category_button)
        btnAddCategory.setOnClickListener {
            val intent = Intent(this, CreateCategory::class.java)
            startActivityForResult(intent, RESPONSE_CATEGORY)
        }

        updateRecyclerView()
        logStateChange("onCreate")
    }

    private fun updateRecyclerView() {
        recylerView.adapter = recyclerAdapter
        // Zuständig für Positionierung innerhalb RecyclerView
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)
    }

    private fun checkIfEmptyRecyclerView(emptyView: View, emptyImageView: ImageView) {
        if (list.isEmpty()) {
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
            val newItem = Item(R.drawable.ic_baseline_category_24, "$result")

            if (newItem in list) {
                createSnackBar(rootLayout, "Category $result already exists")
            } else {
                list.add(newItem)
                recyclerAdapter.notifyItemInserted(index)
                index++
                createSnackBar(rootLayout, "Category $result added")
            }
        }

        checkIfEmptyRecyclerView(emptyView, emptyImageView)
    }

    private fun createSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("Hide") { snackBar.dismiss() }.show()
    }


    override fun onItemClick(position: Int) {
        val intent = Intent(this, CategoryOverview::class.java)
        val itemCategory = list[position].title
        intent.putExtra(NEW_CATEGORY, itemCategory)
        startActivityForResult(intent, RESPONSE_CATEGORY)
    }

    // Allenfalls für Darkmode etc. nötig !!!
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logStateChange(callback: String) {
        Log.d("MGE.MP.DEBUG", "Method: $callback")
    }
}