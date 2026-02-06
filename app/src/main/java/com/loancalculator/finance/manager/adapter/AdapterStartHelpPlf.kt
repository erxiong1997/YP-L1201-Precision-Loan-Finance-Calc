package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.loancalculator.finance.manager.data.DataStartHelpPlf
import com.loancalculator.finance.manager.databinding.ItemStartHelpPlfBinding

class AdapterStartHelpPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataStartHelpPlf>
) :
    RecyclerView.Adapter<AdapterStartHelpPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemStartHelpPlfBinding.inflate(
            LayoutInflater.from(mAdapterContext),
            parent,
            false
        )
        val holderItem = HolderItem(binding)
        return holderItem
    }

    override fun onBindViewHolder(
        holder: HolderItem,
        position: Int
    ) {
        val data = mListDoData[position]
        holder.mPlfBinding.apply {
            ivHelpImage.load(data.drawableStep)
            tvHelpTitle.text = data.titleStep
            tvHelpDes.text = data.contentDes
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemStartHelpPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}