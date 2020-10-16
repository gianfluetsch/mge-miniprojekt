package ch.ost.rj.mge.miniprojekt.model.storage

import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "category_table")
data class Category(@PrimaryKey val name: String, val description: String? = "", val picture: String? = "")

@Entity(tableName = "item_table")
data class Item(@PrimaryKey val titleItem: String, val categoryTitle: String)

//data class CategoryWithItem(
//    @Embedded val category: Category,
//    @Relation(
//        parentColumn = "title",
//        entityColumn = "categoryTitle"
//    )
//    val inventory: List<Item>
//)


