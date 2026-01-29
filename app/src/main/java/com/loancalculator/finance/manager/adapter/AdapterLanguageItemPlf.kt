package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.videodownloader.light.downloader.databinding.ItemLanguageItemBinding
import com.videodownloader.light.downloader.ltddata.DataLtdLanguage

class AdapterLanguageItemPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataLtdLanguage>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterLanguageItemPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemLanguageItemBinding.inflate(
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
        holder.mLtdBinding.apply {
            tvLanguageName.text = data.name
            holder.itemView.isSelected = data.fingerSelect
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mLtdBinding: ItemLanguageItemBinding) :
        RecyclerView.ViewHolder(mLtdBinding.root)

}