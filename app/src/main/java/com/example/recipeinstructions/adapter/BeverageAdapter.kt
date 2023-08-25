package com.example.recipeinstructions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.recipeinstructions.base.BaseRecyclerViewAdapter
import com.example.recipeinstructions.base.BaseViewHolder
import com.example.recipeinstructions.databinding.RcvItemBeverageBinding
import com.example.recipeinstructions.model.Beverage

class BeverageAdapter(
    private val context: Context,
    val onClick: (Beverage) -> Unit
) :
    BaseRecyclerViewAdapter<Beverage, BeverageAdapter.ViewHolder>() {

    private var mListData: MutableList<Beverage> = ArrayList()

    inner class ViewHolder(private val binding: RcvItemBeverageBinding) :
        BaseViewHolder<Beverage>(binding) {

        override fun bindViewHolder(data: Beverage) {
            itemView.setOnClickListener {
                onClick.invoke(data)
            }
            Glide.with(binding.root.context).load(data.image).into(binding.rcvItemImage)
            binding.rcvNameBeverage.text = data.type
        }
    }

    override fun setData(mList: ArrayList<Beverage>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<Beverage> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = RcvItemBeverageBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
    }
}
