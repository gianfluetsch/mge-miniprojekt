package ch.ost.rj.mge.miniprojekt.model

import androidx.lifecycle.LiveData
import ch.ost.rj.mge.miniprojekt.model.storage.Dao

class Repository (private val itemDao: Dao){
    val allItemsAsc: LiveData<List<Item>> = itemDao.getItemsAsc()
    val allItemsDesc: LiveData<List<Item>> = itemDao.getItemsDesc()
    val dateItemsAsc: LiveData<List<Item>> = itemDao.getDateAsc()
    val dateItemsDesc: LiveData<List<Item>> = itemDao.getDateDesc()
    val allFavorites: LiveData<List<Item>> = itemDao.getFavorites()
    val sumItems: LiveData<Int> = itemDao.isDBEmpty()

    suspend fun insert(item: Item) {
        itemDao.insert(item)
    }

    suspend fun insertReplace(item: Item) {
        itemDao.insertReplace(item)
    }

    suspend fun deleteCategory(item: Item) {
        itemDao.deleteItem(item)
    }
}