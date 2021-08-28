package jie.wen.transurbancodetest.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import jie.wen.transurbancodetest.model.dao.StarShipDataDao
import jie.wen.transurbancodetest.model.entity.StarshipDataEntity

@Database(entities = [StarshipDataEntity::class], version = 1)
abstract class StarShipsDatabase : RoomDatabase() {
    abstract fun starshipDataDao(): StarShipDataDao
}