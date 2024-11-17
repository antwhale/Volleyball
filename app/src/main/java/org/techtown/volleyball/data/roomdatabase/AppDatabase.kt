package org.techtown.volleyball.data.roomdatabase

import androidx.room.Database
import androidx.room.RoomDatabase
import org.techtown.volleyball.data.entity.TeamInfo
import org.techtown.volleyball.data.dao.TeamInfoDao
import org.techtown.volleyball.data.dao.UserInfoDao
import org.techtown.volleyball.data.entity.UserInfo

@Database(entities = [TeamInfo::class, UserInfo::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun teamInfoDao(): TeamInfoDao

    abstract fun userInfoDao(): UserInfoDao
}