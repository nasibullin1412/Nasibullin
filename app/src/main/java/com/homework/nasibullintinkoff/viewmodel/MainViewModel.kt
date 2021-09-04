package com.homework.nasibullintinkoff.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.homework.nasibullintinkoff.data.PostDto
import com.homework.nasibullintinkoff.repo.MainDataRepo
import com.homework.nasibullintinkoff.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainDataRepo
): ViewModel() {

    val postDto: LiveData<Resource<PostDto>> get() = _postData
    private val _postData = MutableLiveData<Resource<PostDto>>()

    fun doGetRemoteData(){
        viewModelScope.launch {
            repository.getRemoteData()
                .catch { e ->
                    _postData.value = Resource.error(e.toString())
                }.collect {
                    _postData.value = it
                }
        }
    }
}