package com.apple.glantrox.kajeean_app

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.models.CommentsResponseItem
import kotlinx.coroutines.launch

class CommentsViewModel: ViewModel()
{

    private val _listComments = MutableLiveData<List<CommentsResponseItem>>()
    val listComments: LiveData<List<CommentsResponseItem>> = _listComments
    private val _loadingKajian = MutableLiveData<Boolean>()
    val api = SupabaseAPI.create()

    fun getListComments(kajianid: String) {
        viewModelScope.launch {
            try {
                _loadingKajian.postValue(true)
                val getComments = api.getAllComments("*",kajianid)
                _loadingKajian.postValue(false)
                _listComments.postValue(getComments)
            } catch (e:Exception) {

            }

        }

    }

}