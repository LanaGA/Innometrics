package com.rsf.innometrics.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsf.innometrics.vo.Stats
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Interface for database access for Statistics related operations.
 */
@Dao
interface StatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stats: Stats): Completable

    @Query("SELECT * FROM Stats")
    fun getAll(): LiveData<List<Stats>>

    @Query("select * from Stats order by -time_end LIMIT 1\n")
    fun getLast(): LiveData<Stats>

    @Query("DELETE FROM Stats")
    fun erase(): Completable

}