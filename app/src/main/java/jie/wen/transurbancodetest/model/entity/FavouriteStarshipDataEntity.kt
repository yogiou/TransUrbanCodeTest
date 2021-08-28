package jie.wen.transurbancodetest.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// TODO: will use if need to store in local DB, for storing if the user favourite a starship by the starship url
@Entity
data class FavouriteStarshipDataEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "favourite") val favourite: Boolean
)
