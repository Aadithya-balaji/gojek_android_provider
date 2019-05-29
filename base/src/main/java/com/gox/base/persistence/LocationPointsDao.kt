package com.gox.base.persistence

import androidx.room.*

@Dao
interface LocationPointsDao {

    @Query("SELECT * FROM LocationPointsEntity")
    fun getAllPoints(): List<LocationPointsEntity>

    @Insert
    fun insertPoint(vararg point: LocationPointsEntity)

    @Query("DELETE FROM LocationPointsEntity")
    fun deleteAllPoint()

}