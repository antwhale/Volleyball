package org.techtown.volleyball.hilt

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.techtown.volleyball.constant.APP_DATABASE
import org.techtown.volleyball.data.dao.TeamInfoDao
import org.techtown.volleyball.data.dao.UserInfoDao
import org.techtown.volleyball.data.roomdatabase.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) : AppDatabase =
        Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, APP_DATABASE).build()

    @Singleton
    @Provides
    fun provideTeamInfoDao(database: AppDatabase): TeamInfoDao = database.teamInfoDao()

    @Singleton
    @Provides
    fun provideUserInfoDao(database: AppDatabase): UserInfoDao = database.userInfoDao()

}