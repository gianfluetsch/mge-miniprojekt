package ch.ost.rj.mge.miniprojekt.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.ost.rj.mge.miniprojekt.R
import ch.ost.rj.mge.miniprojekt.model.storage.Category
import ch.ost.rj.mge.miniprojekt.model.storage.Dao
import ch.ost.rj.mge.miniprojekt.model.storage.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): Dao

//    abstract fun itemDao(): Dao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: InventoryDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): InventoryDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, InventoryDatabase::class.java, "inventory_database")
                    .addCallback(InventoryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class InventoryDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
//            INSTANCE?.let { database ->
//                scope.launch {
//                    populateDatabase(database.categoryDao())
//                }
//            }
        }

        suspend fun populateDatabase(categoryDao: Dao) {
            // Delete all content here.
            categoryDao.deleteAll()

            // Add sample words.
//            var category = Category("Hello")
//            categoryDao.insert(category)
//            category = Category("World!")
//            categoryDao.insert(category)

        }
    }
}