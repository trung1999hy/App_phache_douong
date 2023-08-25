package com.example.recipeinstructions.ui.category

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentCategoryBinding
import com.example.recipeinstructions.adapter.CategoryAdapter
import com.example.recipeinstructions.model.TypeRequest
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.ui.inapp.PurchaseInAppActivity
import com.example.recipeinstructions.ui.instructions.FragmentInstructions

class FragmentCategory : BaseFragment<FragmentCategoryBinding>() {
    companion object {
        fun newInstance(type: String? = null): FragmentCategory {
            val fragment = FragmentCategory()
            fragment.type = type
            return fragment
        }
    }

    private var type: String? = ""

    private lateinit var binding: FragmentCategoryBinding
    private lateinit var mAdapter: CategoryAdapter
    private val viewModel: FragmentCategoryViewModel by viewModels()

    override fun initView(view: View) {
        binding = FragmentCategoryBinding.bind(view)
        actionView()
    }

    private fun actionView() {
        getCoin()
        binding.coin.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
        viewModel.type = type
        mAdapter = CategoryAdapter(requireContext(), onClick = {
            replaceFragment(
                R.id.fragment_layout,
                FragmentInstructions.newInstance(it._id),
                FragmentInstructions::class.java.simpleName
            )
        })
        binding.ImgBackSearch.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.RcvCategory.adapter = mAdapter
        viewModel.type?.let {viewModel.getDataToList(it) }
        binding.RcvCategory.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)

        viewModel.listCategory.observe(viewLifecycleOwner, Observer { t ->
            mAdapter.setData(t)
        })

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if(it) {
                binding.PrgCategory.visibility = View.VISIBLE
            } else {
                binding.PrgCategory.visibility = GONE
            }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            if (it == "") {
                binding.RcvCategory.visibility = View.VISIBLE
                binding.LllNoData.visibility = GONE
            } else {
                viewModel.message.observe(viewLifecycleOwner) { message ->
                    binding.TextError.text = message
                }
                binding.LllNoData.visibility = View.VISIBLE
                binding.PrgCategory.visibility = GONE
                binding.RcvCategory.visibility = GONE
            }
        }
    }
    fun getCoin(){
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }
    override fun getBinding(): FragmentCategoryBinding {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return binding
    }

}