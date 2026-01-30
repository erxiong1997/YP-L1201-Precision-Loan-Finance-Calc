package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ItemWorldTimeSelectPlfBinding

class AdapterWorldTimeSelectPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataUtcSelectPlf>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterWorldTimeSelectPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemWorldTimeSelectPlfBinding.inflate(
            LayoutInflater.from(mAdapterContext),
            parent,
            false
        )
        val holderItem = HolderItem(binding)
        holderItem.itemView.setOnClickListener {
            tilFunBack(holderItem.absoluteAdapterPosition)
        }
        return holderItem
    }

    override fun onBindViewHolder(
        holder: HolderItem,
        position: Int
    ) {
        val data = mListDoData[position]
        holder.mPlfBinding.apply {
            tvTimeUtc.text = data.utcPlf
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemWorldTimeSelectPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}