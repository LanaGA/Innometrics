package com.rsf.innometrics.di

import android.app.Application
import androidx.room.Room
import com.rsf.innometrics.data.db.AppDb
import com.rsf.innometrics.data.db.StatsDao
import dagger.Module
import dagger.Provides
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class AppModule {
    @Singleton
    @Provides
    fun provideDb(app: Application): AppDb {
        return Room
                .databaseBuilder(app, AppDb::class.java, "innometrics.db")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

    @Singleton
    @Provides
    fun provideStatsDao(db: AppDb): StatsDao {
        return db.statsDao()
    }
}
