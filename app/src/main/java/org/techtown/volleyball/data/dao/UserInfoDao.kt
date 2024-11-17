package org.techtown.volleyball.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.techtown.volleyball.data.entity.UserInfo

@Dao
interface UserInfoDao {
    @Query("SELECT * FROM UserInfo WHERE id = 1")
    suspend fun getUserInfo(): UserInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfo: UserInfo)

    @Update
    suspend fun updateUserInfo(userInfo: UserInfo)
}