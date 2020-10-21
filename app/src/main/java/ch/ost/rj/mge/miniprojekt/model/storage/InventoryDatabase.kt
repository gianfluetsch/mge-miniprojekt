package ch.ost.rj.mge.miniprojekt.model.storage

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ch.ost.rj.mge.miniprojekt.model.Category
import kotlinx.coroutines.CoroutineScope

@Database(entities = [Category::class], version = 1, exportSchema = false)
abstract class InventoryDatabase : RoomDatabase() {

    abstract fun categoryDao(): Dao

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

        }
    }
}