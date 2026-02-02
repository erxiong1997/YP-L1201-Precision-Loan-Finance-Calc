package com.loancalculator.finance.manager.activity.other

import androidx.recyclerview.widget.LinearLayoutManager
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterCurrencyUnitItemPlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityCurrencyUnitPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf

class PlfCurrencyUnitActivity : PlfBindingActivity<ActivityCurrencyUnitPlfBinding>(
    mBarTextWhite = false
) {
    private lateinit var mAdapterCurrencyUnitItemPlf: AdapterCurrencyUnitItemPlf

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_currency_unit)
        mDataCurrencyUnitPlf?.let {
            for (d in mListData) {
                if (d.currencyUnit == it.currencyUnit) {
                    d.fingerSelect = true
                    break
                }
            }
        }
        setPlfRecyclerView()

        mPlcBinding.topSetPlf.ivSelect.setSafeListener {
            for (data in mListData) {
                if (data.fingerSelect) {
                    mDataCurrencyUnitPlf = data
                    setResult(RESULT_OK)
                    finish()
                    break
                }
            }
        }
    }

    override fun setPlfRecyclerView() {
        mAdapterCurrencyUnitItemPlf = AdapterCurrencyUnitItemPlf(this, mListData) {
            val data = mListData[it]
            if (data.fingerSelect) return@AdapterCurrencyUnitItemPlf
            for (i in mListData.indices) {
                if (mListData[i].fingerSelect) {
                    mListData[i].fingerSelect = false
                    mAdapterCurrencyUnitItemPlf.notifyItemChanged(i)
                    break
                }
            }
            data.fingerSelect = true
            mAdapterCurrencyUnitItemPlf.notifyItemChanged(it)
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterCurrencyUnitItemPlf
    }

    override fun getLayoutValue(): ActivityCurrencyUnitPlfBinding {
        return ActivityCurrencyUnitPlfBinding.inflate(layoutInflater)
    }

    private val mListData = mutableListOf(
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_aud, "AUD", "Australian Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_brl, "BRL", "Brazilian Real", "R$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_cad, "CAD", "Canadian Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_chf, "CHF", "Swiss Franc", "CHF"),
        DataCurrencyUnitPlf(
            R.drawable.plf_til_currency_unit_cny,
            "CNY",
            "Chinese Renminbi Yuan",
            "¥"
        ),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_czk, "CZK", "Czech Koruna", "Kč"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_dkk, "DKK", "Danish Krone", "kr"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_eur, "EUR", "Euro", "€"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_gbp, "GBP", "British Pound", "£"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_hkd, "HKD", "Hong Kong Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_huf, "HUF", "Hungarian Forint", "Ft"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_idr, "IDR", "Indonesian Rupiah", "Rp"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_ils, "ILS", "Israeli New Shekel", "₪"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_inr, "INR", "Indian Rupee", "₹"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_isk, "ISK", "Icelandic Króna", "kr"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_jpy, "JPY", "Japanese Yen", "¥"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_krw, "KRW", "South Korean Won", "₩"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_mxn, "MXN", "Mexican Peso", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_myr, "MYR", "Malaysian Ringgit", "RM"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_nok, "NOK", "Norwegian Krone", "kr"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_nzd, "NZD", "New Zealand Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_php, "PHP", "Philippine Peso", "₱"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_pln, "PLN", "Polish Złoty", "zł"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_ron, "RON", "Romanian Leu", "lei"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_sek, "SEK", "Swedish Krona", "kr"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_sgd, "SGD", "Singapore Dollar", "$"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_thb, "THB", "Thai Baht", "฿"),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_try, "TRY", "Turkish Lira", "₺"),
        DataCurrencyUnitPlf(
            R.drawable.plf_til_currency_unit_usd,
            "USD",
            "United States Dollar",
            "$"
        ),
        DataCurrencyUnitPlf(R.drawable.plf_til_currency_unit_zar, "ZAR", "South African Rand", "R")
    )

}