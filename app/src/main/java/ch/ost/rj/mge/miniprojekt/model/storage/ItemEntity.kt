package ch.ost.rj.mge.miniprojekt.model.storage

import androidx.room.Embedded
import androidx.room.Relation

data class categoryWithItem(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryCreatorId"
    )
    val items: List<Item>
)