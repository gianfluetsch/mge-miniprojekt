package ch.ost.rj.mge.miniprojekt.model

import androidx.lifecycle.LiveData
import ch.ost.rj.mge.miniprojekt.model.storage.Dao

class Repository (private val itemDao: Dao){
    val allItemsAsc: LiveData<List<Item>> = itemDao.getItemsAsc()
    val allItemsDesc: LiveData<List<Item>> = itemDao.getItemsDesc()
    val dateItemsAsc: LiveData<List<Item>> = itemDao.getDateAsc()
    val dateItemsDesc: LiveData<List<Item>> = itemDao.getDateDesc()
    val sumItems: LiveData<Integer> = itemDao.isDBEmpty()

    suspend fun insert(item: Item) {
        itemDao.insert(item)
    }

    suspend fun insertReplace(item: Item) {
        itemDao.insertReplace(item)
    }

    fun isDBEmpty() {
        itemDao.isDBEmpty()
    }

    suspend fun deleteCategory(item: Item) {
        itemDao.deleteItem(item)
    }

    fun checkItemExists(itemName: String) {
        itemDao.checkItemExists(itemName)
    }
}