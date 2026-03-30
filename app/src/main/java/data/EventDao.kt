package com.eunjin.personaleventplanner.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Update
    suspend fun updateEvent(event: Event)

    @Delete
    suspend fun deleteEvent(event: Event)

    // 모든 이벤트를 날짜순으로 정렬해서 가져오기 (리스트 화면용)
    @Query("SELECT * FROM events ORDER BY dateTime ASC")
    fun getAllEvents(): Flow<List<Event>>

    // 특정 ID의 이벤트 하나만 가져오기 (수정 화면용)
    @Query("SELECT * FROM events WHERE id = :id")
    fun getEventById(id: Int): Flow<Event>
}