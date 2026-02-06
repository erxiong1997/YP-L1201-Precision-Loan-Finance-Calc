package com.loancalculator.finance.manager.activity.utils

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import coil.load
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.activity.other.PlfCurrencyUnitActivity
import com.loancalculator.finance.manager.data.DataCurrencyRatePlf
import com.loancalculator.finance.manager.data.DataCurrencyUnitPlf
import com.loancalculator.finance.manager.databinding.ActivityToolsSpeedConvertPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.ToolsSpeedConverterUtils
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mCurrencyListData
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mDataCurrencyUnitPlf
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mExchangeRateList
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mRateCurrencyPlf
import com.squareup.moshi.Moshi

class PlfToolsExchangeRateActivity :
    PlfBindingActivity<ActivityToolsSpeedConvertPlfBinding>(mBarTextWhite = false) {
    //https://api.frankfurter.dev/v1/latest?base=CNY&symbols=USD
    private val mUnitSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    if (mCurSelect == "top") {
                        mPlcBinding.tvSelectTop.text = mRateCurrencyPlf?.currencyUnit
                        mPlcBinding.ivCurrencyFlagTop.load(mRateCurrencyPlf?.currencyDrawable)
                        mPlcBinding.tvCurrencyUnitTop.text = mRateCurrencyPlf?.currencySymbol
                        mTopUnitData = mRateCurrencyPlf
                        if (mTopUnitData?.currencyUnit != mBottomUnitData?.currencyUnit) {
                            getCurrencyRateSave(
                                mTopUnitData, mBottomUnitData, false
                            )
                            getCurrencyRateSave(
                                mTopUnitData, mBottomUnitData, true
                            )
                        }
                    }
                    if (mCurSelect == "bottom") {
                        mPlcBinding.tvSelectBottom.text = mRateCurrencyPlf?.currencyUnit
                        mPlcBinding.ivCurrencyFlagBottom.load(mRateCurrencyPlf?.currencyDrawable)
                        mPlcBinding.tvCurrencyUnitBottom.text = mRateCurrencyPlf?.currencySymbol
                        mBottomUnitData = mRateCurrencyPlf
                        if (mTopUnitData?.currencyUnit != mBottomUnitData?.currencyUnit) {
                            getCurrencyRateSave(
                                mTopUnitData, mBottomUnitData, false
                            )
                            getCurrencyRateSave(
                                mTopUnitData, mBottomUnitData, true
                            )
                        }
                    }
                }
            }
        }

    private var mCurSelect = "no"
    private var mTopUnitData: DataCurrencyUnitPlf? = null
    private var mBottomUnitData: DataCurrencyUnitPlf? = null

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_speed_convert)
        mRateCurrencyPlf = mDataCurrencyUnitPlf
        val data1 = mRateCurrencyPlf
        val data2 = mRateCurrencyPlf
        mTopUnitData = data1
        mBottomUnitData = data2
        mPlcBinding.tvSelectTop.text = data1?.currencyUnit
        mPlcBinding.ivCurrencyFlagTop.load(data1?.currencyDrawable)
        mPlcBinding.tvCurrencyUnitTop.text = data1?.currencySymbol

        mPlcBinding.tvSelectBottom.text = data2?.currencyUnit
        mPlcBinding.ivCurrencyFlagBottom.load(data2?.currencyDrawable)
        mPlcBinding.tvCurrencyUnitBottom.text = data2?.currencySymbol

        getCurrencyRateSave(mTopUnitData, mBottomUnitData, false)
        getCurrencyRateSave(mTopUnitData, mBottomUnitData, true)

        mPlcBinding.llSelectTop.setSafeListener {
            mCurSelect = "top"
            mUnitSelectLauncher.launch(
                Intent(this, PlfCurrencyUnitActivity::class.java).apply {
                    putExtra("unitClass", "convert")
                }
            )
        }
        mPlcBinding.llSelectBottom.setSafeListener {
            mCurSelect = "bottom"
            mUnitSelectLauncher.launch(
                Intent(this, PlfCurrencyUnitActivity::class.java).apply {
                    putExtra("unitClass", "convert")
                }
            )
        }

        mPlcBinding.ivConvertFlag.setSafeListener {
            val a = mTopUnitData
            mTopUnitData = mBottomUnitData
            mBottomUnitData = a

            mPlcBinding.tvSelectTop.text = mTopUnitData?.currencyUnit
            mPlcBinding.ivCurrencyFlagTop.load(mTopUnitData?.currencyDrawable)
            mPlcBinding.tvCurrencyUnitTop.text = mTopUnitData?.currencySymbol

            mPlcBinding.tvSelectBottom.text = mBottomUnitData?.currencyUnit
            mPlcBinding.ivCurrencyFlagBottom.load(mBottomUnitData?.currencyDrawable)
            mPlcBinding.tvCurrencyUnitBottom.text = mBottomUnitData?.currencySymbol

            changeConvertValue(false)
            changeConvertValue(true)
        }
        mPlcBinding.etInputValueTop.doAfterTextChanged {
            if (it.isNullOrEmpty()) {
                mPlcBinding.tvCalculate.isEnabled = false
            } else {
                mPlcBinding.tvCalculate.isEnabled = true
            }
        }
        mPlcBinding.tvResetFields.setSafeListener {
            mPlcBinding.etInputValueTop.setText(null)
            mPlcBinding.tvInputValueBottom.text = "0"
        }
        mPlcBinding.tvCalculate.setSafeListener {
            try {
                val valueInput = mPlcBinding.etInputValueTop.text.toString().trim().toDouble()
                mPlcBinding.tvInputValueBottom.text = "test2"
            } catch (_: Exception) {

            }
        }
    }

    private fun getCurrencyRateSave(
        startUnit: DataCurrencyUnitPlf?,
        endUit: DataCurrencyUnitPlf?,
        revert: Boolean
    ) {
        if (startUnit == null || endUit == null) return
        for (data in mExchangeRateList) {
            if (revert) {
                if (data.base == endUit.currencyUnit && data.rateUnit == startUnit.currencyUnit) {
                    return
                }
            } else {
                if (data.base == startUnit.currencyUnit && data.rateUnit == endUit.currencyUnit) {
                    return
                }
            }
        }

        val queue = Volley.newRequestQueue(this)
        val request = object : StringRequest(
            Method.GET,
            "https://api.xxjsonf.dev/v1/latest?base=${
                if (revert) {
                    endUit.currencyUnit
                } else {
                    startUnit.currencyUnit
                }
            }&symbols=${
                if (revert) {
                    startUnit.currencyUnit
                } else {
                    endUit.currencyUnit
                }
            }".replace(
                "xxjsonf",
                "frankfurter"
            ),
            Response.Listener {
                val moshi = Moshi.Builder().build()
                val moshiAdapter = moshi.adapter(DataCurrencyRatePlf::class.java)
                try {
                    moshiAdapter.fromJson(it)?.let { result ->
                        result.rates?.let { rt ->
                            mExchangeRateList.add(result.apply {
                                rateUnit = endUit.currencyUnit
                                rateValue = rt[endUit.currencyUnit] ?: 0.0
                            })
                            changeConvertValue(revert)
                        }
                    }
                } catch (e: Exception) {

                }
            },
            Response.ErrorListener {

            }) {
            override fun getParamsEncoding(): String {
                return "UTF-8"
            }
        }
        queue.add(request)
    }

    private fun changeConvertValue(revert: Boolean) {
        if (revert) {
            for (data in mExchangeRateList) {
                if (data.base == mBottomUnitData?.currencyUnit) {
                    mPlcBinding.tvBottomConvertResult.text =
                        "${data.amount} ${data.base} = ${data.rateValue} ${data.rateUnit}"
                }
            }
        } else {
            for (data in mExchangeRateList) {
                if (data.base == mTopUnitData?.currencyUnit) {
                    mPlcBinding.tvTopConvertResult.text =
                        "${data.amount} ${data.base} = ${data.rateValue} ${data.rateUnit}"
                }
            }
        }
    }

    override fun getLayoutValue(): ActivityToolsSpeedConvertPlfBinding {
        return ActivityToolsSpeedConvertPlfBinding.inflate(layoutInflater)
    }

}