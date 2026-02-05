package com.loancalculator.finance.manager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ItemHistoryCalculatorPlfBinding
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.value.LoanTypePlf

class AdapterHistoryCalculatorPlf(
    var mDeleteModel: Boolean,
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
        val data = mListDoData[holder.absoluteAdapterPosition]
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

                LoanTypePlf.RD -> {
                    mAdapterContext.getString(R.string.plf_recurring_deposit)
                }

                LoanTypePlf.FD -> {
                    mAdapterContext.getString(R.string.plf_fixed_deposit)
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
            if (mDeleteModel) {
                tvDetails.visibility = View.GONE
                ivSelect.visibility = View.VISIBLE
            } else {
                tvDetails.visibility = View.VISIBLE
                ivSelect.visibility = View.GONE
            }
            ivSelect.isSelected = data.fingerSelect
        }
    }

    override fun onBindViewHolder(holder: HolderItem, position: Int, payloads: List<Any?>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            val vre = payloads[0]
            if (vre is String) {
                when (vre) {
                    "updateStatus" -> {
                        val data = mListDoData[holder.absoluteAdapterPosition]
                        if (mDeleteModel) {
                            holder.mPlfBinding.tvDetails.visibility = View.GONE
                            holder.mPlfBinding.ivSelect.visibility = View.VISIBLE
                        } else {
                            holder.mPlfBinding.tvDetails.visibility = View.VISIBLE
                            holder.mPlfBinding.ivSelect.visibility = View.GONE
                        }
                        holder.mPlfBinding.ivSelect.isSelected = data.fingerSelect
                    }

                    "updateOnce" -> {
                        val data = mListDoData[holder.absoluteAdapterPosition]
                        holder.mPlfBinding.ivSelect.isSelected = data.fingerSelect
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemHistoryCalculatorPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}