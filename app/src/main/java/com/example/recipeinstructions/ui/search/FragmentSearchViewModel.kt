package com.example.recipeinstructions.ui.search

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseViewModel
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.model.SearchModel
import com.example.recipeinstructions.network.config.onCallBack
import com.example.recipeinstructions.repository.Repository
import com.example.recipeinstructions.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentSearchViewModel(application: Application) : BaseViewModel(application) {
    val listSearch: MutableLiveData<ArrayList<Instruction>> = MutableLiveData()
    private val repository = Repository.getInstance(application)

    fun searchDrink(key: String) {
        scope.launch(Dispatchers.IO) {
            try {
                Constants.loadJSONFromAsset(getApplication(), "type/bartending.drinks.json")
                    ?.let {
                        val data = Constants.parseJSONToListInstruction(it)
                        listSearch.postValue(data.filter { it.title.contains(key) }
                            .toMutableList() as ArrayList<Instruction>)
                    }
            } catch (e: Exception) {
                System.err.println(e.message.toString())
            }
        }
    }
}