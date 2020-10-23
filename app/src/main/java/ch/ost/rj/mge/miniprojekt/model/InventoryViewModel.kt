package ch.ost.rj.mge.miniprojekt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.ost.rj.mge.miniprojekt.model.storage.Dao
import ch.ost.rj.mge.miniprojekt.model.storage.InventoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val checkDB: LiveData<Integer>
    private val itemsDao: Dao = InventoryDatabase.getDatabase(application, viewModelScope).categoryDao()

    val allItemsAsc: LiveData<List<Item>>
    val allItemsDesc: LiveData<List<Item>>
    val dateItemsAsc: LiveData<List<Item>>
    val dateItemDesc: LiveData<List<Item>>

    init {
        repository = Repository(itemsDao)
        allItemsAsc = repository.allItemsAsc
        allItemsDesc = repository.allItemsDesc
        dateItemsAsc = repository.dateItemsAsc
        dateItemDesc = repository.dateItemsDesc
        checkDB = repository.sumItems
    }

    fun insert(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(item)
    }

    fun insertReplace(item: Item) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertReplace(item)
    }

    fun DBCount() : LiveData<Integer> {
        return checkDB
    }

    fun deleteItem(item: Item) = viewModelScope.launch (Dispatchers.IO) {
        repository.deleteCategory(item)
    }

    fun checkItemExist(itemName: String) : LiveData<Integer> {
        val checkItem = itemsDao.checkItemExists(itemName)
        return checkItem

    }
}
