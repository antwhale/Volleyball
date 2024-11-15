package org.techtown.volleyball.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class BasePagingAdapter<Item : Any, V: ViewDataBinding>(
    diffUtil: DiffUtil.ItemCallback<Item>
) : PagingDataAdapter<Item, BaseRecyclerViewHolder<V>>(diffUtil) {
    protected val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<V> {
        val inflater = LayoutInflater.from(parent.context)
        val binding = createBinding(inflater, parent, viewType)
        return BaseRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<V>, position: Int) {
        getItem(position)?.let { bind(holder.binding, it, position) }
        holder.binding.root.setOnClickListener { clickItem(holder.binding, position) }
        holder.binding.executePendingBindings()
    }

    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): V
    abstract fun bind(binding: V, item: Item, position: Int)
    open fun clickItem(binding: V, position: Int) {}
}