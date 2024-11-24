package org.techtown.volleyball.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.techtown.volleyball.data.entity.TeamInfo

@Dao
interface TeamInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(teamInfo: TeamInfo)

    @Query("SELECT * FROM TeamInfo")
    suspend fun getAllTeamInfo(): List<TeamInfo>

    @Query("SELECT * FROM TeamInfo WHERE id = :teamId")
    suspend fun getTeamInfo(teamId: Int) : TeamInfo?
}
