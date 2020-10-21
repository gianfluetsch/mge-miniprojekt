package ch.ost.rj.mge.miniprojekt.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ch.ost.rj.mge.miniprojekt.model.storage.InventoryDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InventoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: Repository
    val checkDB: LiveData<List<Integer>>

    val allCategorys: LiveData<List<Category>>

    init {
        val categorysDao = InventoryDatabase.getDatabase(application, viewModelScope).categoryDao()
        repository = Repository(categorysDao)
        allCategorys = repository.allCategorys
        checkDB = repository.sumItems
    }

    fun insert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }

    fun DBCount() : LiveData<List<Integer>> {
        return checkDB
    }
}
