package com.faiz.bkksmkn6.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.faiz.bkksmkn6.data.ApiClient
import com.faiz.bkksmkn6.data.Berita
import com.faiz.bkksmkn6.data.ListResponse
import com.faiz.bkksmkn6.data.Lowongan
import com.faiz.bkksmkn6.data.Perusahaan
import com.faiz.bkksmkn6.util.ApiError
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

abstract class PagedViewModel<T>: ViewModel(){
    private val _items = MutableLiveData<List<T>>(emptyList())
    val items: LiveData<List<T>> = _items
    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> = _loading
    private val _loadingMore= MutableLiveData(false)
    val loadingMore: LiveData<Boolean> = _loadingMore
    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    var query: String? = null
    var type : String?= null

    private var started = false
    private var page=0
    private var totalPages =1
    private var job: Job? = null

    protected abstract suspend fun fetch(page: Int,perPage: Int): ListResponse<T>

    fun loadInitial(){
        if (started)return
        started=true
        refresh()
    }

    fun refresh(clear: Boolean = false){
        started=true
        job?.cancel()
        page=0
        totalPages=1
        _loadingMore.value =  false
        if (clear) _items.value = emptyList()
        load()
    }
    fun loadMore(){
        if(_loading.value == true || _loadingMore.value == true) return
        if (page == 0 || page >= totalPages)return
        load()
    }
    fun clearError() {
        _error.value = null
    }
    private fun load(){
        val next = page+1
        if (next == 1) _loading.value=true else _loadingMore.value = true
        _error.value = null
        job = viewModelScope.launch {
            try {
                val res = fetch(next, PER_PAGE)
                page= res.meta?.page?:next
                totalPages = res.meta?.totalPages?:1
                val baru = res.data.orEmpty()
                _items.value = if (next == 1) baru else _items.value.orEmpty()+baru
            }catch (e: CancellationException){
                throw e
            }catch (e: Exception){
                _error.value= ApiError.message(e)
            }finally {
                if (isActive){
                    _loading.value = false
                    _loadingMore.value = false
                }
            }
        }
    }
    companion object{
        const val PER_PAGE=10
    }
}
private fun String?.orNullIfBlank(): String? = this?.trim()?.takeIf { it.isNotEmpty() }

class JobsViewModel : PagedViewModel<Lowongan>() {
    override suspend fun fetch(page: Int, perPage: Int) =
        ApiClient.api.jobs(page, perPage, query.orNullIfBlank(), type.orNullIfBlank())
}

class NewsViewModel : PagedViewModel<Berita>() {
    override suspend fun fetch(page: Int, perPage: Int) =
        ApiClient.api.news(page, perPage, query.orNullIfBlank())
}

class CompaniesViewModel : PagedViewModel<Perusahaan>() {
    override suspend fun fetch(page: Int,perPage: Int) =
        ApiClient.api.companies(page,perPage, query.orNullIfBlank())
}