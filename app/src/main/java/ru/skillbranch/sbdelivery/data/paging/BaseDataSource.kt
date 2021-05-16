package ru.skillbranch.sbdelivery.data.paging

import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import ru.skillbranch.sbdelivery.ui.main.adapters.CardItem

abstract class BaseDataSource(
    var page: Int = 1,
    val limit: Int = 10
): RxPagingSource<Int, CardItem>() {
    override fun getRefreshKey(state: PagingState<Int, CardItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}