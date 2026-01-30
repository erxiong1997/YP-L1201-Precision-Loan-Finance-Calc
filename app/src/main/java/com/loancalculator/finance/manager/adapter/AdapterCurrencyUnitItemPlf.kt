package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.data.DataPlfLanguage
import com.loancalculator.finance.manager.databinding.ItemCurrencyUnitItemPlfBinding
import com.loancalculator.finance.manager.databinding.ItemLanguageItemPlfBinding

class AdapterCurrencyUnitItemPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataCurrencyUnitPlf>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterCurrencyUnitItemPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemCurrencyUnitItemPlfBinding.inflate(
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
            ivCurrencyUnit.load(data.currencyDrawable) {

            }
            tvCurrencyUnit.text = data.currencyUnit
            tvCurrencyName.text = data.currencyName
            holder.itemView.isSelected = data.fingerSelect
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemCurrencyUnitItemPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}