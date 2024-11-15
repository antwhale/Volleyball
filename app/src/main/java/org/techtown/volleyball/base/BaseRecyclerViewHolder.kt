package org.techtown.volleyball.base

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class BaseRecyclerViewHolder<V : ViewDataBinding>(
    val binding: V
) : RecyclerView.ViewHolder(binding.root)