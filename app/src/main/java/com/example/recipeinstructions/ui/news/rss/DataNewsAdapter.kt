package com.example.recipeinstructions.ui.news.rss

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.recipeinstructions.base.BaseRecyclerViewAdapter
import com.example.recipeinstructions.base.BaseViewHolder
import com.example.recipeinstructions.databinding.ItemNewsBinding
import com.example.recipeinstructions.model.RssItemModel

class DataNewsAdapter(private val context: Context, val onClickItem: (RssItemModel) -> Unit) :
    BaseRecyclerViewAdapter<RssItemModel, DataNewsAdapter.ViewHolder>() {

    private var listDataNews: MutableList<RssItemModel> = ArrayList()

    inner class ViewHolder(private val binding: ItemNewsBinding) :
        BaseViewHolder<RssItemModel>(binding) {
        override fun bindViewHolder(data: RssItemModel) {
            itemView.setOnClickListener { onClickItem.invoke(data) }
            binding.itemTextTitleNews.text = data.title
        }

    }

    override fun setData(mList: ArrayList<RssItemModel>) {
        listDataNews = mList
        notifyDataSetChanged()
    }

    override fun getListItem(): MutableList<RssItemModel> = listDataNews

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemNewsBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
       return listDataNews.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindViewHolder(listDataNews[position])
    }
}