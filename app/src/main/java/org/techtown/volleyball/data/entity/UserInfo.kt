package org.techtown.volleyball.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserInfo(
    @PrimaryKey val id: String,
    val lastDateFetchSchedule: String,      //마지막으로 배구경기 스케줄 데이터 가져온 날짜 (yyyy-MM-dd) 형식
    val favoriteTeam : Int
)


//@Entity
//data class TeamInfo(
//    @PrimaryKey val id: Int,
//    val naverTVUrl: String,
//    val instaUrl: String,
//    val newsUrl: String
//)
