package com.rsf.innometrics

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.room.Room
import com.rsf.innometrics.data.db.AppDb
import androidx.appcompat.widget.Toolbar
import kotlinx.android.synthetic.main.activity_app_usage_statistics.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_usage_statistics)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        val db: AppDb = Room.databaseBuilder(applicationContext, AppDb::class.java, "stats")
                .fallbackToDestructiveMigration()
                .build()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, MainFragment.newInstance(db))
                    .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SettingsFragment())
                    .addToBackStack(null)
                    .commit()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }

    }

    //    override fun onContextItemSelected(item: MenuItem?): Boolean {
//        supportFragmentManager.beginTransaction()
//                .add(R.id.container, SettingsFragment.newInstance())
//                .commit()
//        return super.onContextItemSelected(item)
//    }

}