package com.eunjin.personaleventplanner.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "events")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val category: String, // Work, Social, Travel 등
    val location: String,
    val dateTime: Long    // 정렬을 위해 시간 데이터를 숫자(Long) 형태로 저장합니다.
)