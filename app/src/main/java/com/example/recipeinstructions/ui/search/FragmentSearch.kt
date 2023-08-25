package com.example.recipeinstructions.ui.search

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recipeinstructions.R
import com.example.recipeinstructions.adapter.SearchAdapter
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentSearchBinding
import com.example.recipeinstructions.ui.instructions.FragmentInstructions
import com.example.recipeinstructions.utils.Constants

class FragmentSearch : BaseFragment<FragmentSearchBinding>(), View.OnClickListener {
    companion object {
        fun newInstance() = FragmentSearch()
    }

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mAdapter: SearchAdapter
    private val viewModel: FragmentSearchViewModel by activityViewModels()

    override fun initView(view: View) {
        binding = FragmentSearchBinding.bind(view)
        actionView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun actionView() {
        binding.EditTextSearch.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.EditTextSearch.compoundDrawablesRelative[2]
                if (drawableEnd != null) {
                    if (event.x.toInt() >= v.width - drawableEnd.bounds.width() - v.paddingEnd
                        && event.x.toInt() <= v.width - v.paddingEnd
                        && event.y.toInt() >= (v.height - drawableEnd.bounds.height()) / 2
                        && event.y.toInt() <= (v.height + drawableEnd.bounds.height()) / 2
                    ) {
                        binding.EditTextSearch.setText(Constants.IS_EMPTY)
                        return@setOnTouchListener true
                    }
                }
            }
            false
        }

        binding.EditTextSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()){
                    mAdapter.setData(arrayListOf())
                }else{
                    viewModel.searchDrink(p0.toString())
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.LllCocktail.setOnClickListener(this)
        binding.LllCoffee.setOnClickListener(this)
        binding.LllSoda.setOnClickListener(this)
        binding.LllYogurt.setOnClickListener(this)

        mAdapter = SearchAdapter(requireContext(), onClick = {
            replaceFragment(
                R.id.fragment_layout,
                FragmentInstructions.newInstance(it._id),
                FragmentInstructions::class.java.simpleName
            )
        })
        binding.RcvSearch.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.RcvSearch.adapter = mAdapter

        viewModel.listSearch.observe(requireActivity(), Observer { t -> mAdapter.setData(t) })
    }

    override fun getBinding(): FragmentSearchBinding {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.LllCocktail -> {
                binding.EditTextSearch.setText(resources.getText(R.string.cocktail))
            }

            R.id.LllSoda -> {
                binding.EditTextSearch.setText(resources.getText(R.string.soda))
            }

            R.id.LllCoffee -> {
                binding.EditTextSearch.setText(resources.getText(R.string.coffee))
            }

            R.id.LllYogurt -> {
                binding.EditTextSearch.setText(resources.getText(R.string.yogurt))
            }
        }
    }
}