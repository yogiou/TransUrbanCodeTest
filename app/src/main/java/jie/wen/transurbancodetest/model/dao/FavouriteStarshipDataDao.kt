package jie.wen.transurbancodetest.model.dao

import androidx.room.*
import jie.wen.transurbancodetest.model.entity.FavouriteStarshipDataEntity
import jie.wen.transurbancodetest.model.entity.StarshipDataEntity

// TODO: will use if need to store in local DB
@Dao
interface FavouriteStarshipDataDao {
    @Insert
    fun insert(vararg favouriteStarshipDataEntity: FavouriteStarshipDataEntity)

    @Update
    fun update(vararg favouriteStarshipDataEntity: FavouriteStarshipDataEntity)

    // TODO: define later
    @Delete
    fun delete(favouriteStarshipDataEntity: FavouriteStarshipDataEntity)

    // TODO: define later
    @Query("select * from favouriteStarshipDataEntity")
    fun findAll(): List<FavouriteStarshipDataEntity>
}