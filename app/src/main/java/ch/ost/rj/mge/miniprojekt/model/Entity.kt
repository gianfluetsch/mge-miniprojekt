package ch.ost.rj.mge.miniprojekt.model

import android.widget.ImageView
import androidx.annotation.NonNull
import androidx.room.*
import java.util.*

@Entity(tableName = "item_table")
data class Item(@PrimaryKey val name: String, val description: String, val picture: String, val date: String)


