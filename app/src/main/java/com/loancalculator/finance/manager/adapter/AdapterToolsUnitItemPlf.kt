package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.data.DataPlfLanguage
import com.loancalculator.finance.manager.data.DataUnitSelectPlf
import com.loancalculator.finance.manager.databinding.ItemCurrencyUnitItemPlfBinding
import com.loancalculator.finance.manager.databinding.ItemLanguageItemPlfBinding
import com.loancalculator.finance.manager.databinding.ItemToolsUnitItemPlfBinding

class AdapterToolsUnitItemPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataUnitSelectPlf>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterToolsUnitItemPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemToolsUnitItemPlfBinding.inflate(
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
            tvUnit.text = data.symbol
            tvName.text = data.displayName
            holder.itemView.isSelected = data.fingerSelect
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemToolsUnitItemPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}