package com.example.recipeinstructions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.recipeinstructions.base.BaseRecyclerViewAdapter
import com.example.recipeinstructions.base.BaseViewHolder
import com.example.recipeinstructions.databinding.RcvItemSearchBinding
import com.example.recipeinstructions.model.Instruction
import com.example.recipeinstructions.model.SearchModel

class SearchAdapter(
    private val context: Context,
    val onClick: (Instruction) -> Unit
) :
    BaseRecyclerViewAdapter<Instruction, SearchAdapter.ViewHolder>() {

    private var mListData: ArrayList<Instruction> = ArrayList()

    inner class ViewHolder(private val binding: RcvItemSearchBinding) :
        BaseViewHolder<Instruction>(binding) {

        override fun bindViewHolder(data: Instruction) {
            itemView.setOnClickListener {
                onClick.invoke(data)
            }
            Glide.with(binding.root.context).load(data.image).into(binding.rcvImageSearch)
            binding.rcvTitleSearch.text = data.title
            binding.rcvContentSearch.text = data.ingredient[0]
        }
    }

    override fun setData(mList: ArrayList<Instruction>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): ArrayList<Instruction> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = RcvItemSearchBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
    }
}
