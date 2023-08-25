package com.example.recipeinstructions.ui.category

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeinstructions.base.BaseViewModel
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentCategoryViewModel(application: Application) : BaseViewModel(application) {
    val listCategory: MutableLiveData<ArrayList<Instruction>> = MutableLiveData()
    var type: String? = null

    fun getDataToList(type : String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Constants.loadJSONFromAsset(getApplication(), "type/bartending.drinks.json")
                    ?.let {
                        val data = Constants.parseJSONToListInstruction(it)
                        listCategory.postValue(data.filter { it.type_drink == type }
                            .toMutableList() as ArrayList<Instruction>)
                    }
            } catch (e: Throwable) {

            }
        }
    }


}