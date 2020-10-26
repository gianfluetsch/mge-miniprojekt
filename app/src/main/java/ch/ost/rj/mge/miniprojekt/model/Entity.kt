package ch.ost.rj.mge.miniprojekt.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(@PrimaryKey val name: String, val description: String, val picture: String, val date: String)


