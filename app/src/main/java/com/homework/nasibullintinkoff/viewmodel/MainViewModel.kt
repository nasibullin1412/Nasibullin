package com.homework.nasibullintinkoff.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.repo.MainDataRepo
import com.homework.nasibullintinkoff.utils.NetworkConstants.PAGE_NUMBER
import com.homework.nasibullintinkoff.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainDataRepo
): ViewModel() {

    companion object{
        private const val SHIMMER_WAIT = 1000
    }
    enum class Category(val value: Long, val category: String){
        LATEST(0, "latest"),
        TOP(1, "top"),
        HOT(2, "hot")
    }

    private var currentPostIndex: Long = 0
    var isFromLocal: Boolean = true
    private var currentCategory: Category = Category.LATEST
    private var currentPageNumber: Long = 0


    val postDto: LiveData<Resource<List<PostDto>>> get() = _postData
    private val _postData = MutableLiveData<Resource<List<PostDto>>>()
    val signalBackButton: LiveData<Boolean> get() =_signalBackButton
    private val _signalBackButton = MutableLiveData<Boolean>()
    val signalShimmer: LiveData<Boolean> get() =_signalShimmer
    private val _signalShimmer = MutableLiveData<Boolean>()


    /**
     * asynchronous request to fetch data from the database
     */
    fun doGetLocalData(){
        viewModelScope.launch {
            isFromLocal = true
            repository.getLocalData(currentPostIndex, currentCategory.value)
                .catch { e ->
                    _postData.value = Resource.error(e.toString())
                }.collect {
                    _postData.value = it
                }
        }
    }

    /**
     * asynchronous request to fetch data from the backend
     */
    fun doGetRemoteData(){
        viewModelScope.launch {
            isFromLocal = false
            _signalShimmer.value = true
            delay(SHIMMER_WAIT.toLong())
            repository.getRemoteData(currentCategory.category, currentPageNumber)
                .catch { e ->
                    _postData.value = Resource.error(e.toString())
                }.collect {
                    _postData.value = it
                }
            delay(SHIMMER_WAIT.toLong())
            _signalShimmer.value = false
        }
    }

    /**
     *  asynchronous data insertion into the database
     */
    fun doInsertDatabase(postDtoList: List<PostDto>){
        viewModelScope.launch {
            repository.insertToDatabase(postDtoList, currentPostIndex, currentCategory.value)
        }
    }

    fun doDeleteAllCache(){
        viewModelScope.launch {
            repository.deleteAll(category = currentCategory.value)
            currentPostIndex = 0
            currentPageNumber = 0
            _signalBackButton.value = false
        }
    }

    fun decreaseCurrentPostIndex(): Boolean {
        when (currentPostIndex) {
            0.toLong() -> return false
            1.toLong() -> {
                _signalBackButton.value = false
            }
        }
        currentPostIndex -= 1
        return true
    }

    fun increaseCurrentPostIndex(){
        if (currentPostIndex == 0.toLong()){
            _signalBackButton.value = true
        }
        currentPostIndex += 1
        if (currentPostIndex % PAGE_NUMBER == 0.toLong()){
            currentPageNumber +=1
        }
    }

    fun changeCategory(newCategoryId: Int){
        when(newCategoryId){
            0 -> currentCategory = Category.LATEST
            1 -> currentCategory = Category.TOP
            2 -> currentCategory = Category.HOT
        }
        currentPostIndex = 0
        currentPageNumber = 0
        _signalBackButton.value = false
    }
}