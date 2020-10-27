package ch.ost.rj.mge.miniprojekt.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.adapter.RecyclerAdapter
import ch.ost.rj.mge.miniprojekt.model.InventoryViewModel
import ch.ost.rj.mge.miniprojekt.model.Item
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_overview.*

class Overview : AppCompatActivity(), RecyclerAdapter.OnItemClickListener,
    RecyclerAdapter.CellClickListener {

    companion object {
        const val DESCRIPTION = "description"
        const val PICTURE = "picture"
        const val DATE = "date"
        const val RESPONSE_ITEM = 0
        const val ITEM = "item"
        const val MESSAGE = "message"
        const val GALLERY_REQUEST = 7
        const val CAMERA_REQUEST = 8
        const val MODIFY = "modify"
        const val FAVORITE = "favorite"
        var darkModeCurrent = false
        var filterMode = 0
        var navViewMode = R.id.action_all
        var themeModePref = false
        var filterModePref = 0
        var size = 0
        const val filePath = "ch.ost.rj.mge.miniprojekt.preferences"
        const val DARK_MODE = "darkmode"
        const val FILTER = "filter"
        const val NAVVIEW = "navview"
        private var PRIVATE_MODE = 0
    }

    private val recyclerAdapter = RecyclerAdapter(this, this)
    private lateinit var emptyView: TextView
    private lateinit var emptyImageView: ImageView
    private lateinit var btnAddItem: FloatingActionButton
    private lateinit var itemViewModel: InventoryViewModel
    private lateinit var item: Item
    private lateinit var btnFilterItems: Button
    private lateinit var navigationView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_overview)
        setSupportActionBar(findViewById(R.id.toolbar))

        navigationView = findViewById(R.id.nav_view)
        emptyView = findViewById(R.id.empty_view)
        emptyImageView = findViewById(R.id.empty_imageView)
        btnFilterItems = findViewById(R.id.button_filter)
        btnAddItem = findViewById(R.id.add_item_button)

        itemViewModel = ViewModelProvider(this).get(InventoryViewModel::class.java)
        btnAddItem.setOnClickListener {
            val intent = Intent(this, CreateItem::class.java)
            startActivityForResult(intent, RESPONSE_ITEM)
        }
        btnFilterItems.setOnClickListener {
            filterItems()
        }

        updateRecyclerView()
        getDBSize()
        getThemeMode()
        getFilterMode()
        getNavViewMode()

        navigationView.setOnNavigationItemSelectedListener { item ->
            setNavView(item.itemId)
            true
        }
    }

    private fun setNavView(itemId: Int) {
        when (itemId) {
            R.id.action_all -> {
                navViewMode = R.id.action_all
                btnFilterItems.isVisible = true
                changeFilter(filterModePref)
                setNavViewMode(itemId)
            }
            R.id.action_favorites -> {
                navViewMode = R.id.action_favorites
                btnFilterItems.isVisible = false
                itemViewModel.allFavorites.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
                setNavViewMode(itemId)
            }
        }
    }

    private fun filterItems() {
        val options = resources.getStringArray(R.array.filter_options)
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle(R.string.select_filter_dialog)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> changeFilter(0)
                    1 -> changeFilter(1)
                    2 -> changeFilter(2)
                    3 -> changeFilter(3)
                    4 -> changeFilter(4)
                }
            }
        val dialog = builder.create()
        dialog.show()
    }


    private fun getNavViewMode() {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        val navViewModePref = preference.getInt(NAVVIEW, 0)

        if (navViewModePref != navViewMode) {
            navigationView.selectedItemId = navViewModePref
            setNavView(navViewModePref)
        }
    }

    private fun setNavViewMode(id: Int) {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        val editor = preference.edit()
        editor.putInt(NAVVIEW, id)
        editor.commit()
    }

    private fun getThemeMode() {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        themeModePref = preference.getBoolean(DARK_MODE, false)

        if (themeModePref != darkModeCurrent) {
            changeTheme()
        }
    }

    private fun getFilterMode() {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        filterModePref = preference.getInt(FILTER, 0)

        if (filterModePref != filterMode) {
            changeFilter(filterModePref)
        } else {
            changeFilter(filterModePref)
        }
    }

    private fun changeFilter(filter: Int) {
        when (filter) {
            0 -> {
                filterMode = 0
                itemViewModel.allItemsAsc.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
            }
            1 -> {
                filterMode = 1
                itemViewModel.allItemsDesc.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
            }
            2 -> {
                filterMode = 2
                itemViewModel.sortFavorites.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
            }
            3 -> {
                filterMode = 3
                itemViewModel.dateItemDesc.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
            }
            4 -> {
                filterMode = 4
                itemViewModel.dateItemsAsc.observe(this, { items ->
                    items?.let { recyclerAdapter.setItems(it) }
                })
            }
        }
        changeFilterMode()
    }

    @SuppressLint("ApplySharedPref")
    private fun changeFilterMode() {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        val editor = preference.edit()
        editor.putInt(FILTER, filterMode)
        editor.commit()
    }

    @SuppressLint("ApplySharedPref")
    private fun setThemeMode() {
        val preference: SharedPreferences = getSharedPreferences(filePath, PRIVATE_MODE)
        val editor = preference.edit()
        editor.putBoolean(DARK_MODE, darkModeCurrent)
        editor.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    private fun updateRecyclerView() {
        recylerView.adapter = recyclerAdapter
        recylerView.layoutManager = LinearLayoutManager(this)
        recylerView.setHasFixedSize(true)
    }

    private fun getDBSize() {
        itemViewModel.checkDB.observe(this, { items ->
            items?.let { waitForObserver(it) }
        })
    }

    private fun waitForObserver(dbSize: Int) {
        size = dbSize
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
        if (requestCode == RESPONSE_ITEM && resultCode == RESULT_OK) {
            val message = data?.getStringExtra(MESSAGE)
            data?.getStringExtra(ITEM)?.let {
                if (message != null) {
                    createSnackBar(rootLayout, message)
                }
                Unit
            }
        }
        getDBSize()
    }

    override fun onItemClick(item: Item, position: Int) {
        this.item = item
        loadContentFromRoom()
    }

    private fun loadContentFromRoom() {
        val intent = Intent(this, ItemOverview::class.java)
        val itemTitle = item.name
        val itemDescription = item.description
        val picture = item.picture
        val date = item.date
        val favorite = item.favorite
        intent.putExtra(ITEM, itemTitle)
        intent.putExtra(DESCRIPTION, itemDescription)
        intent.putExtra(PICTURE, picture)
        intent.putExtra(DATE, date)
        intent.putExtra(FAVORITE, favorite)
        startActivityForResult(intent, RESPONSE_ITEM)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                changeTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun changeTheme() {
        if (darkModeCurrent) {
            darkModeCurrent = false
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            delegate.applyDayNight()
        } else {
            darkModeCurrent = true
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            delegate.applyDayNight()
        }
        setThemeMode()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val item = menu!!.findItem(R.id.action_settings)
        if (darkModeCurrent) {
            item.title = "Light Mode"
        } else {
            item.title = "Dark Mode"
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCellClickListener(item: Item, button: Button) {
        itemViewModel.checkFavorite(item.name)
            .observeOnce(this, { items ->
                items?.let {
                    if (it == 0) {
                        button.setBackgroundResource(R.drawable.ic_baseline_favorite_24)
                        val item = Item(item.name, item.description, item.picture, item.date, 1)
                        itemViewModel.insertReplace(item)
                    } else {
                        button.setBackgroundResource(R.drawable.ic_baseline_favorite_border_24)
                        val item = Item(item.name, item.description, item.picture, item.date, 0)
                        itemViewModel.insertReplace(item)
                    }
                }
            })
    }


    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    private fun createSnackBar(view: View, message: String) {
        val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snackBar.setAction("Hide") { snackBar.dismiss() }.show()
    }
}