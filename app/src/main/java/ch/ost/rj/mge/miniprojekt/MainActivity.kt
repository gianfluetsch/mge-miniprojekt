package ch.ost.rj.mge.miniprojekt

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_category.*
import kotlinx.android.synthetic.main.activity_overview.*

class MainActivity : AppCompatActivity(), Adapter.OnItemClickListener {
    val RESPONSE_CATEGORY = 0
    val NEW_CATEGORY = "category"
    private var list = ArrayList<Item>()
    var index = 0

    private val adapter = Adapter(list, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(findViewById(R.id.toolbar))

        val btnAddCategory = findViewById<FloatingActionButton>(R.id.add_category_button)
        btnAddCategory.setOnClickListener {
            val intent = Intent(this, CreateCategory::class.java)
            startActivityForResult(intent, RESPONSE_CATEGORY)
        }

        updateRecyclerView()
        logStateChange("onCreate")
    }

    fun updateRecyclerView() {
        recylerView.adapter = adapter
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESPONSE_CATEGORY && resultCode == RESULT_OK) {
            val result = data?.getStringExtra(NEW_CATEGORY)
            val newItem = Item(R.drawable.ic_baseline_category_24, "$result")

            if (newItem in list) {
                createSnackbar(rootLayout, "Category $result already exists")
            } else {
                list.add(newItem)
                adapter.notifyItemInserted(index)
                index++
                createSnackbar(rootLayout, "Category $result added")
            }
        }
    }

    private fun createSnackbar(view: View, message: String) {
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackbar.setAction("Hide") { snackbar.dismiss() }.show()
    }


    override fun onItemClick(position: Int) {
//        Toast.makeText(this, "Item ${list.get(position).title} clicked", Toast.LENGTH_SHORT).show()
//        val clickedItem = list[position]
////        clickedItem.title = "Clicked"
//        adapter.notifyItemChanged(position)
        val intent = Intent(this, CategoryOverview::class.java)
        val itemCategory = list[position].title
        intent.putExtra(NEW_CATEGORY, "$itemCategory")
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