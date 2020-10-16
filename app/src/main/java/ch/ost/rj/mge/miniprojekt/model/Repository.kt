package ch.ost.rj.mge.miniprojekt.model

import androidx.lifecycle.LiveData
import ch.ost.rj.mge.miniprojekt.model.storage.Category
import ch.ost.rj.mge.miniprojekt.model.storage.Dao

class Repository (private val categoryDao: Dao){
    val allCategorys: LiveData<List<Category>> = categoryDao.getAlphabetizedWords()



    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }
}