package com.example.recipeinstructions.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.recipeinstructions.base.BaseRecyclerViewAdapter
import com.example.recipeinstructions.base.BaseViewHolder
import com.example.recipeinstructions.databinding.RcvItemFavoriteBinding
import com.example.recipeinstructions.model.Favourite

class FavoriteAdapter(
    private val context: Context,
    val onClick: (Favourite) -> Unit,
    val onClickTrash: (Favourite) -> Unit
) :
    BaseRecyclerViewAdapter<Favourite, FavoriteAdapter.ViewHolder>() {

    private var mListData: MutableList<Favourite> = ArrayList()

    inner class ViewHolder(private val binding: RcvItemFavoriteBinding) :
        BaseViewHolder<Favourite>(binding) {

        override fun bindViewHolder(data: Favourite) {
            itemView.setOnClickListener {
                onClick.invoke(data)
            }
            binding.ImgDelete.setOnClickListener { onClickTrash.invoke(data) }
            Glide.with(binding.root.context).load(data.image).into(binding.rcvImageFavorite)
            binding.rcvNameFavorite.text = data.title
        }
    }

    override fun setData(mList: ArrayList<Favourite>) {
        mListData = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<Favourite> = mListData

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val mBinding = RcvItemFavoriteBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding = mBinding)
    }

    override fun getItemCount(): Int {
        return mListData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(mListData[position])
    }
}
