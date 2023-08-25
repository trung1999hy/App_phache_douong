package com.example.recipeinstructions.ui.instructions

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.recipeinstructions.R
import com.example.recipeinstructions.base.BaseFragment
import com.example.recipeinstructions.databinding.FragmentInstructionsBinding
import com.example.recipeinstructions.model.Favourite
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.ui.MainApp
import com.example.recipeinstructions.ui.category.FragmentCategory
import com.example.recipeinstructions.ui.inapp.PurchaseInAppActivity

class FragmentInstructions : BaseFragment<FragmentInstructionsBinding>() {
    companion object {
        fun newInstance(id: String? = null): FragmentInstructions {
            val fragment = FragmentInstructions()
            fragment.id = id
            return fragment
        }
    }

    var id: String? = null

    private lateinit var binding: FragmentInstructionsBinding
    private val viewModel: FragmentInstructionViewModel by viewModels()

    override fun initView(view: View) {
        binding = FragmentInstructionsBinding.bind(view)
        actionView()
    }

    private fun actionView() {
        getCoin()
        binding.coin.setOnClickListener {
            startActivity(Intent(requireActivity(), PurchaseInAppActivity::class.java))
        }
        viewModel.isFavourite.observe(viewLifecycleOwner) {
            if (!it) {

                binding.ButtonAddFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.favorite_border_black, 0, 0, 0
                )
                binding.ImgFavoriteSearch.setImageResource(R.drawable.favorite_border)
            } else {
                binding.ButtonAddFavorite.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.favorite_black, 0, 0, 0
                )
                binding.ImgFavoriteSearch.setImageResource(R.drawable.favorite)
            }
        }

        binding.ImgBackSearch.setOnClickListener {
            replaceFragment(
                R.id.fragment_layout,
                FragmentCategory.newInstance(),
                FragmentCategory::class.java.simpleName
            )
        }
        binding.ButtonAddFavorite.setOnClickListener(onClickListener)
        binding.ImgFavoriteSearch.setOnClickListener(onClickListener)
        binding.ImgBackSearch.setOnClickListener {
            activity?.onBackPressedDispatcher?.onBackPressed()
        }
        viewModel.getDataToList().observe(viewLifecycleOwner) { list ->
            viewModel.instructions.value?.let { instruction ->
                viewModel.isFavourite.postValue(list.any {
                    it._id == instruction._id
                })
            }

        }

        viewModel.id = id ?: ""
        viewModel.detailDrink(viewModel.id)
        viewModel.instructions.observe(viewLifecycleOwner) {
            var ingredient = ""
            var instruction = ""
            for (i in it.ingredient) {
                ingredient += i + "\n"
                Log.d(TAG, "actionView: $ingredient")
            }
            for (i in it.intructions) {
                instruction += i + "\n"
            }
            binding.TextIngredient.text = ingredient
            binding.TextInstructions.text = instruction
            binding.TextTitleSearch.text = it.title
            Glide.with(this@FragmentInstructions).load(it.image).into(binding.ImgImageInstructions)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) {
                binding.ScrollInstruction.visibility = GONE
                binding.PrgInstruction.visibility = VISIBLE
            } else {
                binding.ScrollInstruction.visibility = VISIBLE
                binding.PrgInstruction.visibility = GONE
            }
        }

        viewModel.message.observe(viewLifecycleOwner) {
            if (it == "") {
                binding.ScrollInstruction.visibility = VISIBLE
                binding.LllNoData.visibility = GONE
            } else {
                viewModel.message.observe(viewLifecycleOwner) { message ->
                    binding.TextError.text = message
                }
                binding.ScrollInstruction.visibility = GONE
                binding.LllNoData.visibility = VISIBLE
                binding.PrgInstruction.visibility = GONE
            }
        }
    }

    fun getCoin() {
        binding.coin.text = MainApp.getInstant()?.preference?.getValueCoin().toString()
    }

    override fun getBinding(): FragmentInstructionsBinding {
        binding = FragmentInstructionsBinding.inflate(layoutInflater)
        return binding
    }

    private val onClickListener = View.OnClickListener {
        if (viewModel.isFavourite.value == true) {

            binding.ImgFavoriteSearch.setImageResource(R.drawable.favorite_border)
            viewModel.instructions.value?.let {
                viewModel.remove(Favourite(it._id, it.type_drink, it.title, it.image))
            }
        } else {
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setMessage("Bạn có muốn thêm  tốn 1 vàng không ?")
                .setTitle("Thêm vào yêu thích ?")
            dialog.setPositiveButton("Oke") { dialog, which ->
                MainApp.getInstant()?.preference?.apply {
                    if (getValueCoin() > 0) {
                        setValueCoin(getValueCoin() - 1)
                        Toast.makeText(
                            requireContext(),
                            "Đã thêm  thành công và trù 1 vàng",
                            Toast.LENGTH_SHORT

                        ).show()
                        binding.ButtonAddFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.favorite_border_black, 0, 0, 0
                        )
                        binding.ButtonAddFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            R.drawable.favorite_black, 0, 0, 0
                        )
                        binding.ImgFavoriteSearch.setImageResource(R.drawable.favorite)

                        viewModel.instructions.value?.let {
                            viewModel.addDataToList(
                                Favourite(
                                    it._id,
                                    it.type_drink,
                                    it.title,
                                    it.image
                                )
                            )
                        }   
                        getCoin()
                    } else startActivity(
                        Intent(
                            requireActivity(),
                            PurchaseInAppActivity::class.java
                        )
                    )
                }
            }
            dialog.show()

        }
    }
}

