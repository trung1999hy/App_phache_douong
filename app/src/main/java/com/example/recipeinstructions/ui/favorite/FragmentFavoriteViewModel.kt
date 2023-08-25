package com.example.recipeinstructions.ui.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseViewModel
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.model.OverallModel
import com.example.recipeinstructions.repository.Repository
import com.example.recipeinstructions.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentFavoriteViewModel(application: Application) : BaseViewModel(application) {
    private val repository = Repository(application)
    val listFavorite: MutableLiveData<ArrayList<Favourite>> = MutableLiveData()

     fun getDataToList(): LiveData<List<Favourite>> = repository.getLiveData()

    fun remove(item: Favourite){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.remove(item)
            } catch (e: Throwable) {

            }
        }
    }
}