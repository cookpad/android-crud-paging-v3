package com.cookpad.crud_paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlin.random.Random

class SampleViewModel : ViewModel() {
    private val modificationEvents = MutableStateFlow<List<SampleViewEvents>>(emptyList())

    private val combined =
        Pager(PagingConfig(pageSize = SampleRepository.PAGE_SIZE)) { SamplePagingSource(SampleRepository()) }
            .flow
            .cachedIn(viewModelScope)
            .combine(modificationEvents) { pagingData, modifications ->
                modifications.fold(pagingData) { acc, event ->
                    applyEvents(acc, event)
                }
            }

    val pagingDataViewStates: LiveData<PagingData<SampleEntity>> = combined.asLiveData()

    fun onViewEvent(sampleViewEvents: SampleViewEvents) {
        modificationEvents.value += sampleViewEvents
    }

    private fun applyEvents(
        paging: PagingData<SampleEntity>,
        sampleViewEvents: SampleViewEvents
    ): PagingData<SampleEntity> {
        return when (sampleViewEvents) {
            is SampleViewEvents.Remove -> {
                paging
                    .filter { sampleViewEvents.sampleEntity.id != it.id }
            }
            is SampleViewEvents.Edit -> {
                paging
                    .map {
                        if (sampleViewEvents.sampleEntity.id == it.id) return@map it.copy(name = "${it.name} (updated)")
                        else return@map it
                    }
            }
            SampleViewEvents.InsertItemHeader -> {
                paging.insertHeaderItem(

                    item = SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the top"
                    )
                )
            }
            SampleViewEvents.InsertItemFooter -> {
                paging.insertFooterItem(
                    item = SampleEntity(
                        id = Random.nextInt(0, 1000),
                        name = "New item added at the bottom"
                    )
                )
            }
        }
    }
}
