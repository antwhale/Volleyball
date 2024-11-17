package org.techtown.volleyball.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TeamInfo(
    @PrimaryKey val id: Int,
    val naverTVUrl: String,
    val instaUrl: String,
    val newsUrl: String
)
