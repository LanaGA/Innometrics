package com.rsf.innometrics.db

import androidx.room.Database
import com.rsf.innometrics.AppStats
import androidx.room.RoomDatabase

/**
 * Main database description.
 */
@Database(
    entities = [
        AppStats::class
        ],
    version = 3,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}
