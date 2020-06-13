package com.rsf.innometrics.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsf.innometrics.vo.Stats
import io.reactivex.Completable

/**
 * Interface for database access for Statistics related operations.
 */
@Dao
interface StatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stats: Stats): Completable

    @Query("SELECT * FROM Stats")
    fun getAll(): LiveData<List<Stats>>

}