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

    val allCategorys: LiveData<List<Category>>

    init {
        val categorysDao = InventoryDatabase.getDatabase(application, viewModelScope).categoryDao()
        repository = Repository(categorysDao)
        allCategorys = repository.allCategorys
    }

    fun insert(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(category)
    }
}