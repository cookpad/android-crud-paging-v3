package com.cookpad.crud_paging

sealed class SampleViewEvents {
    data class Edit(val sampleEntity: SampleEntity) : SampleViewEvents()
    data class Remove(val sampleEntity: SampleEntity) : SampleViewEvents()
    object InsertItemHeader : SampleViewEvents()
    object InsertItemFooter : SampleViewEvents()
}


