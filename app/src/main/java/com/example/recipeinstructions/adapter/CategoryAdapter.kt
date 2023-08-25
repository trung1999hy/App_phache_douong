package com.example.recipeinstructions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.example.recipeinstructions.R
import com.bumptech.glide.Glide
import com.example.recipeinstructions.base.BaseRecyclerViewAdapter
import com.example.recipeinstructions.base.BaseViewHolder
import com.example.recipeinstructions.databinding.RcvItemCategoryBinding
import com.example.recipeinstructions.model.Instruction

class CategoryAdapter(
    private val context: Context,
    val onClick: (Instruction) -> Unit
) :
    BaseRecyclerViewAdapter<Instruction, CategoryAdapter.ViewHolder>() {

    private var mListData: MutableList<Instruction> = ArrayList()

    inner class ViewHolder(private val binding: RcvItemCategoryBinding) :
        BaseViewHolder<Instruction>(binding) {

        override fun bindViewHolder(data: Instruction) {
            itemView.setOnClickListener {
                onClick.invoke(data)
            }
            Glide.with(binding.root.context).load(data.image).into(binding.rcvImageCategory)
            binding.rcvNameCategory.text = data.title
        }
    }

    override fun setData(mList: ArrayList<Instruction>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<Instruction> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = RcvItemCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.rcv_slide_in_left))
    }
}
