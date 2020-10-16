package ch.ost.rj.mge.miniprojekt.model.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao


@Dao
public interface Dao {

    @Query("SELECT * from category_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<Category>>

    // ignores a new word if it's exactly the same as one already in the list
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Query("DELETE FROM category_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteCategory(category: Category)

//    @Query("SELECT * from item_table ORDER BY titleItem ASC")
//    fun getAlphabetizedItems(): LiveData<List<Item>>
//
////     ignores a new word if it's exactly the same as one already in the list
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    suspend fun insert(item: Item)
//
//    @Query("DELETE FROM item_table")
//    suspend fun deleteAllItems()
//
//    @Delete
//    suspend fun deleteItem(item: Item)

//    @Transaction
//    @Query("SELECT * FROM category_table")
//    fun getCategoryWithItem(): List<CategoryWithItem>
}