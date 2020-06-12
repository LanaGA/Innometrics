package com.rsf.innometrics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.rsf.innometrics.db.AppDb


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_usage_statistics)

        val db: AppDb = Room.databaseBuilder(applicationContext, AppDb::class.java, "stats")
                .fallbackToDestructiveMigration()
                .build()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, MainFragment.newInstance(db))
                    .commit()
        }
    }
}