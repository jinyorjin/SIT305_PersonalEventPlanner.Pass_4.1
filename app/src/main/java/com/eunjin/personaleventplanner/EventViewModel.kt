package com.eunjin.personaleventplanner

import android.app.Application
import androidx.lifecycle.*
import com.eunjin.personaleventplanner.data.Event
import com.eunjin.personaleventplanner.data.EventDatabase
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = EventDatabase.getDatabase(application).eventDao()

    // 리스트 화면에서 관찰할 데이터
    val allEvents: LiveData<List<Event>> = dao.getAllEvents().asLiveData()

    // 새 이벤트 저장
    fun insert(event: Event) = viewModelScope.launch {
        dao.insertEvent(event)
    }

    // 기존 이벤트 수정
    fun update(event: Event) = viewModelScope.launch {
        dao.updateEvent(event)
    }

    // 이벤트 삭제
    fun delete(event: Event) = viewModelScope.launch {
        dao.deleteEvent(event)
    }

    // 수정 화면에서 특정 이벤트를 불러올 때 사용
    fun getEventById(id: Int): LiveData<Event> {
        return dao.getEventById(id).asLiveData()
    }
}