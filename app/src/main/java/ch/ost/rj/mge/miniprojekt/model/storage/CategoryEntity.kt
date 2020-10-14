package ch.ost.rj.mge.miniprojekt.model.storage

import android.widget.ImageView
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey val categoryId: Long,
    val image: ImageView,
    val title: String
)

@Entity
data class Item(
    @PrimaryKey val itemId: Long,
    val categoryCreatorId: Long,
    val title: String,
    val description: String
)