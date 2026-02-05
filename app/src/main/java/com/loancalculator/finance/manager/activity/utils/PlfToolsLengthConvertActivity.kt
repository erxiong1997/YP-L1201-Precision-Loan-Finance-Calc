package com.loancalculator.finance.manager.activity.utils

import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityToolsLenghtConvertPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.LengthUnit
import com.loancalculator.finance.manager.utils.ToolsLengthConvertUtils
import com.loancalculator.finance.manager.utils.value.ParamsLtdUtils.mLengthUnitList

class PlfToolsLengthConvertActivity :
    PlfBindingActivity<ActivityToolsLenghtConvertPlfBinding>(mBarTextWhite = false) {
    private val mUnitSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val position = it.getIntExtra("position", -1)
                    if (position > -1) {
                        val data = mLengthUnitList[position]
                        if (mCurSelect == "top") {
                            mTopPosition = position
                            mPlcBinding.tvSelectTop.text = data.displayName
                            mPlcBinding.tvCurrencyUnitTop.text = data.symbol
                            mTopUnitData = data
                            changeConvertTextShow()
                        }
                        if (mCurSelect == "bottom") {
                            mBottomPosition = position
                            mPlcBinding.tvSelectBottom.text = data.displayName
                            mPlcBinding.tvCurrencyUnitBottom.text = data.symbol
                            mBottomUnitData = data
                            changeConvertTextShow()
                        }
                    }
                }
            }
        }
    private var mTopPosition = 0
    private var mBottomPosition = 2
    private var mCurSelect = "no"
    private var mTopUnitData: LengthUnit? = null
    private var mBottomUnitData: LengthUnit? = null

    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_length_convert)

        val data1 = mLengthUnitList[0]
        val data2 = mLengthUnitList[2]
        mTopUnitData = data1
        mBottomUnitData = data2
        mPlcBinding.tvSelectTop.text = data1.displayName
        mPlcBinding.tvCurrencyUnitTop.text = data1.symbol
        mPlcBinding.tvSelectBottom.text = data2.displayName
        mPlcBinding.tvCurrencyUnitBottom.text = data2.symbol
        changeConvertTextShow()

        mPlcBinding.llSelectTop.setSafeListener {
            mCurSelect = "top"
            mUnitSelectLauncher.launch(
                Intent(this, PlfToolsUnitSelectActivity::class.java).apply {
                    putExtra("unitClass", "length")
                    putExtra("prePosition", mTopPosition)
                }
            )
        }
        mPlcBinding.llSelectBottom.setSafeListener {
            mCurSelect = "bottom"
            mUnitSelectLauncher.launch(
                Intent(this, PlfToolsUnitSelectActivity::class.java).apply {
                    putExtra("unitClass", "length")
                    putExtra("prePosition", mBottomPosition)
                }
            )
        }

        mPlcBinding.ivConvertFlag.setSafeListener {
            val a = mTopUnitData
            mTopUnitData = mBottomUnitData
            mBottomUnitData = a
            val b = mTopPosition
            mTopPosition = mBottomPosition
            mBottomPosition = b


            mPlcBinding.tvCurrencyUnitTop.text = mTopUnitData?.symbol
            mPlcBinding.tvCurrencyUnitBottom.text = mBottomUnitData?.symbol
            mPlcBinding.tvSelectTop.text = mTopUnitData?.displayName
            mPlcBinding.tvSelectBottom.text = mBottomUnitData?.displayName

            changeConvertTextShow()
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
                mPlcBinding.tvInputValueBottom.text =
                    ToolsLengthConvertUtils.convert(valueInput, mTopUnitData, mBottomUnitData)
            } catch (_: Exception) {

            }
        }
    }

    private fun getCurValueResult(reversal: Boolean = false): String {
        var valueInput = -1.0
        try {
            if (!mPlcBinding.etInputValueTop.text.isNullOrEmpty()) {
                valueInput = mPlcBinding.etInputValueTop.text.toString().trim().toDouble()
            }
        } catch (_: Exception) {

        }
        if (valueInput < 0) {
            valueInput = 1.0
        }

        return if (reversal) {
            ToolsLengthConvertUtils.convert(
                value = valueInput,
                fromUnit = mBottomUnitData,
                toUnit = mTopUnitData
            )
        } else {
            ToolsLengthConvertUtils.convert(
                value = valueInput,
                fromUnit = mTopUnitData,
                toUnit = mBottomUnitData
            )
        }

    }

    private fun changeConvertTextShow() {
        mPlcBinding.tvTopConvertResult.text =
            "1 ${mTopUnitData?.displayName} = ${getCurValueResult()} ${mBottomUnitData?.displayName}"
        mPlcBinding.tvBottomConvertResult.text =
            "1 ${mBottomUnitData?.displayName} = ${getCurValueResult(true)} ${mTopUnitData?.displayName}"
        try {
            if (!mPlcBinding.etInputValueTop.text.isNullOrEmpty()) {
                val valueInput = mPlcBinding.etInputValueTop.text.toString().trim().toDouble()
                mPlcBinding.tvInputValueBottom.text =
                    ToolsLengthConvertUtils.convert(valueInput, mTopUnitData, mBottomUnitData)
            }
        } catch (_: Exception) {

        }
    }

    override fun getLayoutValue(): ActivityToolsLenghtConvertPlfBinding {
        return ActivityToolsLenghtConvertPlfBinding.inflate(layoutInflater)
    }

}