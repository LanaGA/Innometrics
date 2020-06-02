package com.rsf.innometrics.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rsf.innometrics.vo.Stats

/**
 * Interface for database access for Statistics related operations.
 */
@Dao
interface StatsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACEw
    fun insert(stats: Stats)

    @Query("SELECT * FROM stats WHERE app_name = :app_name")
    fun findByLogin(app_name: String): LiveData<Stats>
}
