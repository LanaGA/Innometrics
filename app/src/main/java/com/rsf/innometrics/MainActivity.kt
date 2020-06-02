package com.rsf.innometrics

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
//        AndroidInjection.inject(this)
        val appContainer = InnometricsApp()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_usage_statistics)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, MainFragment.newInstance())
                    .commit()
        }
    }
}