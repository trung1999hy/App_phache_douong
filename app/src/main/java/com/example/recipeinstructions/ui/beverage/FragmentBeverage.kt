package com.example.recipeinstructions.ui.beverage

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.JsonReader
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentBeverageBinding
import com.example.recipeinstructions.databinding.FragmentSearchBinding
import com.example.recipeinstructions.adapter.BeverageAdapter
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.ui.category.FragmentCategory
import com.example.recipeinstructions.ui.favorite.FragmentFavorite
import com.example.recipeinstructions.ui.inapp.PurchaseInAppActivity
import com.example.recipeinstructions.ui.search.FragmentSearch
import com.example.recipeinstructions.utils.Constants

class FragmentBeverage : BaseFragment<FragmentBeverageBinding>() {
    companion object {
        fun newInstance() = FragmentBeverage()
    }

    private lateinit var binding: FragmentBeverageBinding
    private lateinit var mAdapter: BeverageAdapter
    private val viewModel: FragmentBeverageViewModel by viewModels()

    override fun initView(view: View) {
        binding = FragmentBeverageBinding.bind(view)
        actionView()
    }

    private fun actionView() {

        getCoin()
        binding.coin.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
        binding.ImageSearch.setOnClickListener {
            replaceFragment(
                R.id.fragment_layout,
                FragmentSearch.newInstance(),
                FragmentSearchBinding::class.java.simpleName
            )
        }

        binding.ImageFavorite.setOnClickListener {
            replaceFragment(
                R.id.fragment_layout,
                FragmentFavorite.newInstance(),
                FragmentFavorite::class.java.simpleName
            )
        }
        binding.RcvBeverage.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        mAdapter = BeverageAdapter(requireContext(), onClick = {
            replaceFragment(
                R.id.fragment_layout,
                FragmentCategory.newInstance(it.type),
                FragmentCategory::class.java.simpleName
            )
        })
        binding.RcvBeverage.adapter = mAdapter
        viewModel.listBeverage.observe(viewLifecycleOwner) {
            mAdapter.setData(it)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.PrgBeverage.visibility = View.VISIBLE
            } else {
                binding.PrgBeverage.visibility = View.GONE
            }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            if (it == "") {
                binding.LllNoData.visibility = View.GONE
            } else {
                viewModel.message.observe(viewLifecycleOwner) { message ->
                    binding.TextError.text = message
                }
                binding.LllNoData.visibility = View.VISIBLE
                binding.PrgBeverage.visibility = View.GONE
            }
        }
    }

    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }

    override fun getBinding(): FragmentBeverageBinding {
        binding = FragmentBeverageBinding.inflate(layoutInflater)
        return binding
    }
}