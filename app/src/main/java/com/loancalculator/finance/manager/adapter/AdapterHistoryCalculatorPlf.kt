package com.loancalculator.finance.manager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.data.DataPlfLanguage
import com.loancalculator.finance.manager.databinding.ItemHistoryCalculatorPlfBinding
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.value.LoanTypePlf

class AdapterHistoryCalculatorPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataPersonalLoanPlf>,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterHistoryCalculatorPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemHistoryCalculatorPlfBinding.inflate(
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

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(
        holder: HolderItem,
        position: Int
    ) {
        val data = mListDoData[position]
        holder.mPlfBinding.apply {
            tvItemName.text = when (data.loanType) {
                LoanTypePlf.PERSONAL -> {
                    mAdapterContext.getString(R.string.plf_personal_loan)
                }

                LoanTypePlf.BUSINESS -> {
                    mAdapterContext.getString(R.string.plf_business_loan)
                }

                LoanTypePlf.MORTGAGES -> {
                    mAdapterContext.getString(R.string.plf_mortgages)
                }

                LoanTypePlf.AUTO -> {
                    mAdapterContext.getString(R.string.plf_auto_loan)
                }

                else -> ""
            }
            tvStartDate.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)
            tvInterest.text = "${data.interestRate}%"
            tvDuration.text = "${data.loanTerm}${
                if (data.loanTermUnit == "month") {
                    mAdapterContext.getString(R.string.plf_month)
                } else {
                    mAdapterContext.getString(R.string.plf_year)
                }
            }"
            tvAmount.text = "${data.loanAmount}${data.currencySymbol}"
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemHistoryCalculatorPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}