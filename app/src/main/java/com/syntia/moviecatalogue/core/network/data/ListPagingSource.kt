package com.syntia.moviecatalogue.core.network.data

import com.syntia.moviecatalogue.core.network.response.data.base.ListItemResponse

class ListPagingSource<T : Any, U : Any>(
    private val serviceMethod: suspend (Int) -> ListItemResponse<T>,
    private val mapper: (ListItemResponse<T>) -> List<U>) : BasePagingSource<T, U>() {

  override suspend fun getResult(page: Int): List<U> {
    val response = serviceMethod.invoke(page)
    return mapper.invoke(response)
  }
}