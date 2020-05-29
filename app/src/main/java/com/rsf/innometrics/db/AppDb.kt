package com.rsf.innometrics.db

import androidx.room.Database
import com.rsf.innometrics.Stats
import androidx.room.RoomDatabase

/**
 * Main database description.
 */
@Database(
    entities = [
        Stats::class
        ],
    version = 3,
    exportSchema = false
)
abstract class AppDb : RoomDatabase() {
    abstract fun statsDao(): StatsDao
}
