package com.loancalculator.finance.manager.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.data.DataPersonalLoanPlf
import com.loancalculator.finance.manager.databinding.ItemCompareMortgagesPlfBinding
import com.loancalculator.finance.manager.formatToFixString
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.TimeDatePlfUtils
import com.loancalculator.finance.manager.utils.value.LoanTypePlf

class AdapterCompareMortgagesPlf(
    private val mAdapterContext: Context,
    private val mListDoData: MutableList<DataPersonalLoanPlf>,
    private val tilDeleteBack: (Int) -> Unit,
    private val tilFunBack: (Int) -> Unit
) :
    RecyclerView.Adapter<AdapterCompareMortgagesPlf.HolderItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HolderItem {
        val binding = ItemCompareMortgagesPlfBinding.inflate(
            LayoutInflater.from(mAdapterContext),
            parent,
            false
        )
        val holderItem = HolderItem(binding)
        holderItem.itemView.setOnClickListener {
            tilFunBack(holderItem.absoluteAdapterPosition)
        }
        binding.tvDeleteTable.setSafeListener {
            tilDeleteBack(holderItem.absoluteAdapterPosition)
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
            tvTableName.text = data.aTableName
            tvLoanAmount2.text =
                "${data.loanAmount}${data.currencySymbol}"
            tvIntersetRate2.text = "${data.interestRate}%"
            tvLoanTerm2.text = if (data.loanTermUnit == "month") {
                "${data.loanTerm} ${mAdapterContext.getString(R.string.plf_month)}"
            } else {
                "${data.loanTerm} ${mAdapterContext.getString(R.string.plf_year)}"
            }
            tvStartDate2.text = TimeDatePlfUtils.getTimeDateOnePlf(data.startDate)

            tvMonthPayment2.text = "${data.monthlyPayment}${data.currencySymbol}"

            val totalPay = data.monthlyPayment * if (data.loanTermUnit == "month") {
                data.loanTerm
            } else {
                data.loanTerm * 12
            }

            tvDownPayment2.text = "${data.firstAmount}${data.currencySymbol}"
            tvPropertyTax2.text = "${data.propertyTax}${data.currencySymbol}"
            tvPMI2.text = "${data.pmiMoney}${data.currencySymbol}"
            tvHomeInsurance2.text = "${data.homeownersInsurance}${data.currencySymbol}"
            tvHOAFees2.text = "${data.hoaMoney}${data.currencySymbol}"

            tvMonthPayment2.text = "${data.monthlyPayment}${data.currencySymbol}"
            tvTotalPayment2.text = "${totalPay.formatToFixString()}${data.currencySymbol}"
        }
    }

    override fun getItemCount(): Int {
        return mListDoData.size
    }

    inner class HolderItem(val mPlfBinding: ItemCompareMortgagesPlfBinding) :
        RecyclerView.ViewHolder(mPlfBinding.root)

}