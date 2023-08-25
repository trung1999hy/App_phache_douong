package com.example.recipeinstructions.ui.instructions

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeinstructions.base.BaseViewModel
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.network.config.onCallBack
import com.example.recipeinstructions.repository.Repository
import com.example.recipeinstructions.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentInstructionViewModel(application: Application) : BaseViewModel(application) {
    private val repository = Repository.getInstance(application)
    var instructions: MutableLiveData<Instruction> = MutableLiveData()
    var id: String = ""
    var isFavourite: MutableLiveData<Boolean> = MutableLiveData(false)
    fun getDataToList(): LiveData<List<Favourite>> = repository.getLiveData()

    fun detailDrink(key: String) {
        scope.launch(Dispatchers.IO) {
            try {
                Constants.loadJSONFromAsset(getApplication(), "type/bartending.drinks.json")
                    ?.let {
                        val data = Constants.parseJSONToListInstruction(it)
                        instructions.postValue(data.filter { it._id == key }.first())
                    }

            } catch (e: Exception) {
                System.err.println(e.message.toString())
                this@FragmentInstructionViewModel.message.postValue(e.msgError)
            }
        }
    }

    fun addDataToList(item: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.add(item)
            } catch (e: Throwable) {

            }
        }
    }

    fun remove(item: Favourite) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.remove(item)
            } catch (e: Throwable) {

            }
        }
    }

}