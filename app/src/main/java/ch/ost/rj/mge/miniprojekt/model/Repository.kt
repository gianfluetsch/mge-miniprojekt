package ch.ost.rj.mge.miniprojekt.model

import androidx.lifecycle.LiveData
import ch.ost.rj.mge.miniprojekt.model.storage.Dao

class Repository (private val categoryDao: Dao){
    val allCategorys: LiveData<List<Category>> = categoryDao.getAlphabetizedWords()
    val sumItems: LiveData<Integer> = categoryDao.isDBEmpty()

    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    fun isDBEmpty() {
        categoryDao.isDBEmpty()
    }

    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }

    fun checkItemExists(itemName: String) {
        categoryDao.checkItemExists(itemName)
    }
}