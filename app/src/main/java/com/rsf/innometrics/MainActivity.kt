package com.rsf.innometrics

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.rsf.innometrics.data.db.AppDb


class MainActivity : AppCompatActivity() {
    val bundle = Bundle()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_usage_statistics)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        val login = intent.getStringExtra("login");
        bundle.putString("login", login)

        val db: AppDb = Room.databaseBuilder(applicationContext, AppDb::class.java, "stats")
                .fallbackToDestructiveMigration()
                .build()

        val mainFragment = MainFragment.newInstance(db)
        mainFragment.arguments = bundle
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, mainFragment)
                    .commit()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            val settingsFragment = SettingsFragment()
            settingsFragment.setArguments(bundle)
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, settingsFragment)
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