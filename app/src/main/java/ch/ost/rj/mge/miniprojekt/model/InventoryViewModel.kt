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
    val categorysDao: Dao

    val allCategorys: LiveData<List<Category>>

    init {
        categorysDao = InventoryDatabase.getDatabase(application, viewModelScope).categoryDao()
        repository = Repository(categorysDao)
        allCategorys = repository.allCategorys
        checkDB = repository.sumItems
    }

    fun insert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }

    fun DBCount() : LiveData<Integer> {
        return checkDB
    }

    fun deleteCategory(category: Category) = viewModelScope.launch (Dispatchers.IO) {
        repository.deleteCategory(category)
    }

//    fun checkItemExist(itemName: String) = viewModelScope.launch (Dispatchers.IO){
//        repository.checkItemExists(itemName)
//    }

    fun checkItemExist(itemName: String) : LiveData<Integer> {
        val checkItem = categorysDao.checkItemExists(itemName)
        return checkItem

    }
}
