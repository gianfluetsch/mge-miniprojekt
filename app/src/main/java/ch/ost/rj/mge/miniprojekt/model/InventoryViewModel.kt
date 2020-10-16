package ch.ost.rj.mge.miniprojekt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.ost.rj.mge.miniprojekt.model.storage.Category
import ch.ost.rj.mge.miniprojekt.model.storage.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
//    private val repositoryItem: RepositoryItem

    val allCategorys: LiveData<List<Category>>
//    val allItems: LiveData<List<ch.ost.rj.mge.miniprojekt.model.storage.Item>>

    init {
        val categorysDao = InventoryDatabase.getDatabase(application, viewModelScope).categoryDao()
        repository = Repository(categorysDao)
        allCategorys = repository.allCategorys
    }

//    init {
//        val itemsDao = InventoryDatabase.getDatabase(application, viewModelScope).itemDao()
//        repositoryItem = RepositoryItem(itemsDao)
////        allItems = repositoryItem.allItems
//    }

    fun insert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }

//    fun insert(item: Item) = viewModelScope.launch(Dispatchers.IO){
////        repositoryItem.insert(item)
//    }
}