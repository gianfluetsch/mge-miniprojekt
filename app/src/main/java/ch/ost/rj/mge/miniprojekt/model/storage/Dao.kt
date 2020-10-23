package ch.ost.rj.mge.miniprojekt.model.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import ch.ost.rj.mge.miniprojekt.model.Item


@Dao
public interface Dao {

    @Query("SELECT * from item_table ORDER BY name ASC")
    fun getItemsAsc(): LiveData<List<Item>>

    @Query("SELECT * from item_table ORDER BY name DESC")
    fun getItemsDesc(): LiveData<List<Item>>

    @Query("SELECT * from item_table ORDER BY date ASC")
    fun getDateAsc(): LiveData<List<Item>>

    @Query("SELECT * from item_table ORDER BY date DESC")
    fun getDateDesc(): LiveData<List<Item>>

    @Query("SELECT COUNT(name) from item_table")
    fun isDBEmpty(): LiveData<Integer>

    @Query("SELECT COUNT(1) FROM item_table WHERE name = :itemName")
    fun checkItemExists(itemName: String): LiveData<Integer>

    // ignores a new word if it's exactly the same as one already in the list
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Item)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReplace(item: Item)

    @Query("DELETE FROM item_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteItem(item: Item)

}