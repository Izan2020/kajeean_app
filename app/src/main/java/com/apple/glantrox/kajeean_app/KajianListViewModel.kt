package com.apple.glantrox.kajeean_app

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apple.glantrox.kajeean_app.api.SupabaseAPI
import com.apple.glantrox.kajeean_app.models.PostKajianResponseItem
import kotlinx.coroutines.launch

class KajianListViewModel: ViewModel() {

    private val _listKajian = MutableLiveData<List<PostKajianResponseItem>>()
    private val _loadingKajian = MutableLiveData<Boolean>()

    val loadingKajian: LiveData<Boolean> = _loadingKajian
    val listKajian: LiveData<List<PostKajianResponseItem>> =  _listKajian
    val api = SupabaseAPI.create()

    private fun getListKajian() {
        _loadingKajian.postValue(true)
        viewModelScope.launch {
            try {
                val getKajian = api.getAllKajian()
                _loadingKajian.postValue(false)
                _listKajian.postValue(getKajian)

            }catch (e:Exception) {
                Log.d("error","${e.message}")
            }
        }

    }





    init {
        getListKajian()
    }

}

