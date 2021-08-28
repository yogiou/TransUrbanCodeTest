package jie.wen.transurbancodetest.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Date

// TODO: will use if need to store in local DB
@Entity
data class StarshipDataEntity (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "model") val model: String?,
    @ColumnInfo(name = "manufacturer") val manufacturer: String?,
    @ColumnInfo(name = "cost_in_credits") val costInCredits: String?,
    @ColumnInfo(name = "length") val length: String?,
    @ColumnInfo(name = "max_atmosphering_speed") val maxAtmospheringSpeed: String?,
    @ColumnInfo(name = "crew") val crew: String?,
    @ColumnInfo(name = "passengers") val passengers: String?,
    @ColumnInfo(name = "cargo_capacity") val cargoCapacity: String?,
    @ColumnInfo(name = "consumables") val consumables: String?,
    @ColumnInfo(name = "hyperdrive_rating") val hyperDriveRating: String?,
    @ColumnInfo(name = "mglt") val mglt: String?,
    @ColumnInfo(name = "starship_class") val starshipClass: String?,
    @ColumnInfo(name = "pilots") val pilots: String?,
    @ColumnInfo(name = "films") val films: String?,
    @ColumnInfo(name = "created") val created: String?,
    @ColumnInfo(name = "edited") val edited: String?,
    @ColumnInfo(name = "url") val url: String?
    )