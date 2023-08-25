package com.example.recipeinstructions.ui.beverage

import android.app.Application
import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeinstructions.base.BaseViewModel
import com.example.recipeinstructions.model.Beverage
import com.example.recipeinstructions.network.config.onCallBack
import com.example.recipeinstructions.repository.Repository
import com.example.recipeinstructions.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentBeverageViewModel(application: Application) : BaseViewModel(application) {
    private val repository = Repository.getInstance(application)
    val listBeverage: MutableLiveData<ArrayList<Beverage>> = MutableLiveData()

    init {
        getDataTypeDrink()
    }

    private fun getDataTypeDrink() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Constants.loadJSONFromAsset(getApplication(), "type/bartending.brewing_genres.json")
                    ?.let { listBeverage.postValue(Constants.parseJSONToListType(it)) }
            } catch (e: Exception) {
                Log.d("lỗi ", e.msgError.toString())
                this@FragmentBeverageViewModel.message.postValue(e.msgError)
            }
        }

    }
}