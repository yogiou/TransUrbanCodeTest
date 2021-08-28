package jie.wen.transurbancodetest.model.dao

import androidx.room.*
import jie.wen.transurbancodetest.model.entity.StarshipDataEntity

// TODO: will use if need to store in local DB
@Dao
interface StarShipDataDao {
    @Insert
    fun insert(vararg airlineDataEntity: StarshipDataEntity)

    @Update
    fun update(vararg airlineDataEntity: StarshipDataEntity)

    // TODO: define later
    @Delete
    fun delete(airlineDataEntity: StarshipDataEntity)

    // TODO: define later
    @Query("select * from starshipDataEntity")
    fun findAll(): List<StarshipDataEntity>
}