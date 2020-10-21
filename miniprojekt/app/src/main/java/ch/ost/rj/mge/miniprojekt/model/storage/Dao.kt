package ch.ost.rj.mge.miniprojekt.model.storage

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.Dao
import ch.ost.rj.mge.miniprojekt.model.Category


@Dao
public interface Dao {

    @Query("SELECT * from category_table ORDER BY name ASC")
    fun getAlphabetizedWords(): LiveData<List<Category>>

    @Query("SELECT COUNT(name) from category_table")
    fun isDBEmpty(): LiveData<List<Integer>>

    // ignores a new word if it's exactly the same as one already in the list
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: Category)

    @Query("DELETE FROM category_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteCategory(category: Category)

}