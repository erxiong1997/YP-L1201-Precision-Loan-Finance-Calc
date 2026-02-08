package com.loancalculator.finance.manager.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.databinding.ItemAmortizationTablePlfBinding
import com.loancalculator.finance.manager.utils.LoanMonthDetail

class AdapterAmortizationTablePlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<LoanMonthDetail>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterAmortizationTablePlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemAmortizationTablePlfBinding.inflate(
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
            tvNumber.text = (position + 1).toString()
            tvPayment.text = data.payment.toString()
            tvInterest.text = data.interestPart.toString()
            tvPrincipal.text = data.principalPart.toString()
            tvBalance.text = data.remainingPrincipal.toString()
            if (position % 2 == 0) {
                holder.itemView.setBackgroundResource(R.color.plf_EFF8F4)
            } else {
                holder.itemView.background = null
            }
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemAmortizationTablePlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}