package com.homework.nasibullintinkoff.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.repo.MainDataRepo
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
    private var currentPostIndex: Long = 0
    var isFromLocal: Boolean = true
    val postDto: LiveData<Resource<PostDto>> get() = _postData
    private val _postData = MutableLiveData<Resource<PostDto>>()
    val signalBackButton: LiveData<Boolean> get() =_signalBackButton
    private val _signalBackButton = MutableLiveData<Boolean>()
    val signalShimmer: LiveData<Boolean> get() =_signalShimmer
    private val _signalShimmer = MutableLiveData<Boolean>()


    fun doGetLocalData(){
        viewModelScope.launch {
            isFromLocal = true
            repository.getLocalData(currentPostIndex)
                .catch { e ->
                    _postData.value = Resource.error(e.toString())
                }.collect {
                    _postData.value = it
                }
        }
    }

    fun doGetRemoteData(){
        viewModelScope.launch {
            isFromLocal = false
            _signalShimmer.value = true
            repository.getRemoteData()
                .catch { e ->
                    _postData.value = Resource.error(e.toString())
                }.collect {
                    _postData.value = it
                }
            delay(SHIMMER_WAIT.toLong())
            _signalShimmer.value = false
        }
    }

    fun doInsertDatabase(postDto: PostDto){
        viewModelScope.launch {
            repository.insertToDatabase(postDto, currentPostIndex)
        }
    }

    fun doDeleteAllCache(){
        viewModelScope.launch {
            repository.deleteAll()
            currentPostIndex = 0
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
    }
}