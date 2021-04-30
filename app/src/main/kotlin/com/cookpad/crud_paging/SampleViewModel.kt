package com.cookpad.crud_paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.insertFooterItem
import androidx.paging.insertHeaderItem
import androidx.paging.map
import kotlin.random.Random

class SampleViewModel : ViewModel() {
    private val _pagingDataViewStates =
        Pager(PagingConfig(pageSize = SampleRepository.PAGE_SIZE)) { SamplePagingSource(SampleRepository()) }.flow
            .cachedIn(viewModelScope)
            .asLiveData()
            .let { it as MutableLiveData<PagingData<SampleEntity>> }

    val pagingDataViewStates: LiveData<PagingData<SampleEntity>> = _pagingDataViewStates

    fun onViewEvent(sampleViewEvents: SampleViewEvents) {
        val paingData = pagingDataViewStates.value ?: return

        when (sampleViewEvents) {
            is SampleViewEvents.Remove -> {
                paingData
                    .filter { sampleViewEvents.sampleEntity.id != it.id }
                    .let { _pagingDataViewStates.value = it }
            }
            is SampleViewEvents.Edit -> {
                paingData
                    .map {
                        if (sampleViewEvents.sampleEntity.id == it.id) return@map it.copy(name = "${it.name} (updated)")
                        else return@map it
                    }
                    .let { _pagingDataViewStates.value = it }
            }
            SampleViewEvents.InsertItemHeader -> {
                _pagingDataViewStates.value = paingData.insertHeaderItem(
                    item =
                    SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the top"
                    )
                )
            }
            SampleViewEvents.InsertItemFooter -> {
                _pagingDataViewStates.value = paingData.insertFooterItem(
                    item =
                    SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the bottom"
                    )
                )
            }
        }
    }
}
