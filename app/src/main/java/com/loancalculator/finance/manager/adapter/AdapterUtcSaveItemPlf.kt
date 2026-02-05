package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.data.DataUtcSelectPlf
import com.loancalculator.finance.manager.databinding.ItemUtcSaveItemPlfBinding
import com.loancalculator.finance.manager.setSafeListener

class AdapterUtcSaveItemPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataUtcSelectPlf>,
    private val tilLongBack: (Int) -> Unit,
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
        holderItem.itemView.setOnLongClickListener {
            tilLongBack(holderItem.absoluteAdapterPosition)
            true
        }
        return holderItem
    }

    override fun onBindViewHolder(
        holder: HolderItem,
        position: Int
    ) {
        val data = mListDoData[holder.absoluteAdapterPosition]
        holder.mPlfBinding.apply {
            tvCurUtc.text = data.utcPlf
            tvCurTime.text = data.mCurTime
            tvCurZone.text = data.utcOffsetValue
            tvCurAmPm.text = data.amOrPm
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemUtcSaveItemPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}