package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.data.DataLtdLanguage
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ItemUtcSaveItemPlfBinding
import com.loancalculator.finance.manager.databinding.ItemWorldTimeSelectPlfBinding
import com.loancalculator.finance.manager.setSafeListener

class AdapterUtcSaveItemPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataUtcSelectPlf>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterUtcSaveItemPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemUtcSaveItemPlfBinding.inflate(
            LayoutInflater.from(mAdapterContext),
            parent,
            false
        )
        val holderItem = HolderItem(binding)
        holderItem.itemView.setSafeListener {
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
            Log.d("TAG", "onBindViewHolder:${data.utcPlf}==${position} ")
            tvCurUtc.text = data.utcPlf
            tvCurTime.text = data.mCurTime
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mLtdBinding: ItemUtcSaveItemPlfBinding) :
        RecyclerView.ViewHolder(mLtdBinding.root)

}