package org.techtown.volleyball.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseListAdapter<Item, V : ViewDataBinding>(
    diffUtil: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, BaseRecyclerViewHolder<V>>(diffUtil) {
    protected val TAG = this::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseRecyclerViewHolder<V> {
        val binding = createBinding(LayoutInflater.from(parent.context), parent, viewType)
        return BaseRecyclerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseRecyclerViewHolder<V>, position: Int) {
        onBind(holder.binding, getItem(position), position)
        holder.binding.root.setOnClickListener { clickItem(holder.binding, position) }
        holder.binding.executePendingBindings()
    }

    override fun submitList(list: MutableList<Item>?) {
        /*thread (start = true){
            super.submitList(list?.let { ArrayList(it) })
        }*/
        super.submitList(list?.let { ArrayList(it) })
    }

    abstract fun createBinding(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): V
    abstract fun onBind(binding: V, item: Item, position: Int)
    open fun clickItem(binding: V, position: Int) {}
}