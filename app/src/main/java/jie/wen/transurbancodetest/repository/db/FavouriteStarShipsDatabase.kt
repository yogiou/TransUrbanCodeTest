package jie.wen.transurbancodetest.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import jie.wen.transurbancodetest.model.dao.FavouriteStarshipDataDao
import jie.wen.transurbancodetest.model.entity.FavouriteStarshipDataEntity

@Database(entities = [FavouriteStarshipDataEntity::class], version = 1)
abstract class FavouriteStarShipsDatabase : RoomDatabase() {
    abstract fun favouriteStarshipDataDao(): FavouriteStarshipDataDao
}