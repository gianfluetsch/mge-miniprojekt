package ch.ost.rj.mge.miniprojekt.model

import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.room.*

@Entity(tableName = "category_table")
data class Category(@PrimaryKey val name: String, val description: String? = "", val picture: String? = "")


