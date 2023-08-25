package com.example.recipeinstructions.base

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseViewHolder<DATA>(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    abstract fun bindViewHolder(data: DATA)
}