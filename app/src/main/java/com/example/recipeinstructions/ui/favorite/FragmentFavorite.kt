package com.example.recipeinstructions.ui.favorite

import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelLazy
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recipeinstructions.R
import com.example.recipeinstructions.adapter.FavoriteAdapter
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentFavoriteBinding
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.ui.inapp.PurchaseInAppActivity
import com.example.recipeinstructions.ui.instructions.FragmentInstructions

class FragmentFavorite : BaseFragment<FragmentFavoriteBinding>() {
    companion object {
        fun newInstance() = FragmentFavorite()
    }

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var mAdapter: FavoriteAdapter
    private val viewModel: FragmentFavoriteViewModel by viewModels()

    override fun initView(view: View) {
        binding = FragmentFavoriteBinding.bind(view)
        actionView()
    }

    private fun actionView() {
        getCoin()
        binding.ImgBackSearch.setOnClickListener {
            activity?.onBackPressed()
        }
        binding.coin.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
        mAdapter = FavoriteAdapter(requireActivity(), onClick = {
            replaceFragment(
                R.id.fragment_layout,
                FragmentInstructions.newInstance(it._id),
                FragmentInstructions::class.java.simpleName
            )
        }, onClickTrash = {
            viewModel.remove(it)
        })
        binding.RcvFavorite.layoutManager =
            StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.RcvFavorite.adapter = mAdapter

        viewModel.getDataToList().observe(viewLifecycleOwner) { it ->
            mAdapter.setData(it.toMutableList() as ArrayList<Favourite>)

            if (it.isEmpty()) {
                binding.TextError.text = "Bạn chưa có công thức pha chế yêu thích nào!"
                binding.LllNoData.visibility = View.VISIBLE
                binding.RcvFavorite.visibility = View.GONE
            } else {
                binding.RcvFavorite.visibility = View.VISIBLE
                binding.LllNoData.visibility = View.GONE
            }
        }
    }
    fun getCoin(){
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }

    override fun getBinding(): FragmentFavoriteBinding {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding
    }
}