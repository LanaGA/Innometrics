package com.rsf.innometrics.db

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

    @Query("SELECT * FROM stats WHERE app_name = :app_name")
    fun findByLogin(app_name: String): LiveData<Stats>
}
