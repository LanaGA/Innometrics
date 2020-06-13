package com.rsf.innometrics.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rsf.innometrics.vo.Stats

/**
 * Main database description.
 */
@Database(
        entities = [Stats::class],
        version = 5,
        exportSchema = false
)
public abstract class AppDb : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}