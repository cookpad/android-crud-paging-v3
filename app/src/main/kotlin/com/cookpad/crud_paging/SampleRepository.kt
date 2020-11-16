package com.cookpad.crud_paging

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class SampleRepository {
    suspend fun getNextPage(index: Int): List<Int> {
        return withContext(Dispatchers.IO) {
            if (index != 1) { // simulate a delay of 2 seconds after the first page
                delay(2000L)
            }

            if (index < PAGE_SIZE * 5) { // simulate a max of 5 pages
                (index..index + PAGE_SIZE).map { it }
            } else {
                emptyList()
            }
        }
    }

    companion object {
        const val PAGE_SIZE = 20
    }
}
