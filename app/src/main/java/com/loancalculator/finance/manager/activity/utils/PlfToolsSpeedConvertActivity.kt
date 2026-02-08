package com.loancalculator.finance.manager.activity.utils

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doAfterTextChanged
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityToolsSpeedConvertPlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.SpeedUnit
import com.loancalculator.finance.manager.utils.ToolsSpeedConverterUtils
import com.loancalculator.finance.manager.utils.value.ParamsPlfUtils.mSpeedUnitList

class PlfToolsSpeedConvertActivity :
    PlfBindingActivity<ActivityToolsSpeedConvertPlfBinding>(mBarTextWhite = false) {
    private val mUnitSelectLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.let {
                    val position = it.getIntExtra("position", -1)
                    if (position > -1) {
                        val data = mSpeedUnitList[position]
                        if (mCurSelect == "top") {
                            mTopPosition = position
                            mPlcBinding.tvSelectTop.text = data.symbol
                            mPlcBinding.tvCurrencyUnitTop.text = data.symbol
                            mTopUnitData = data
                            changeConvertTextShow()
                        }
                        if (mCurSelect == "bottom") {
                            mBottomPosition = position
                            mPlcBinding.tvSelectBottom.text = data.symbol
                            mPlcBinding.tvCurrencyUnitBottom.text = data.symbol
                            mBottomUnitData = data
                            changeConvertTextShow()
                        }
                    }
                }
            }
        }
    private var mTopPosition = 0
    private var mBottomPosition = 3
    private var mCurSelect = "no"
    private var mTopUnitData: SpeedUnit? = null
    private var mBottomUnitData: SpeedUnit? = null

    override fun beginViewAndDoPlf() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_speed_convert)

        val data1 = mSpeedUnitList[0]
        val data2 = mSpeedUnitList[3]
        mTopUnitData = data1
        mBottomUnitData = data2
        mPlcBinding.tvSelectTop.text = data1.symbol
        mPlcBinding.tvCurrencyUnitTop.text = data1.symbol
        mPlcBinding.tvSelectBottom.text = data2.symbol
        mPlcBinding.tvCurrencyUnitBottom.text = data2.symbol
        changeConvertTextShow()

        mPlcBinding.llSelectTop.setSafeListener {
            mCurSelect = "top"
            mUnitSelectLauncher.launch(
                Intent(this, PlfToolsUnitSelectActivity::class.java).apply {
                    putExtra("unitClass", "speed")
                    putExtra("prePosition", mTopPosition)
                }
            )
        }
        mPlcBinding.llSelectBottom.setSafeListener {
            mCurSelect = "bottom"
            mUnitSelectLauncher.launch(
                Intent(this, PlfToolsUnitSelectActivity::class.java).apply {
                    putExtra("unitClass", "speed")
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
            mPlcBinding.tvSelectTop.text = mTopUnitData?.symbol
            mPlcBinding.tvSelectBottom.text = mBottomUnitData?.symbol

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
                    ToolsSpeedConverterUtils.convert(valueInput, mTopUnitData, mBottomUnitData)
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
            ToolsSpeedConverterUtils.convert(
                value = valueInput,
                fromUnit = mBottomUnitData,
                toUnit = mTopUnitData
            )
        } else {
            ToolsSpeedConverterUtils.convert(
                value = valueInput,
                fromUnit = mTopUnitData,
                toUnit = mBottomUnitData
            )
        }

    }

    @SuppressLint("SetTextI18n")
    private fun changeConvertTextShow() {
        mPlcBinding.tvTopConvertResult.text =
            "1 ${mTopUnitData?.symbol} = ${getCurValueResult()} ${mBottomUnitData?.symbol}"
        mPlcBinding.tvBottomConvertResult.text =
            "1 ${mBottomUnitData?.symbol} = ${getCurValueResult(true)} ${mTopUnitData?.symbol}"
        try {
            if (!mPlcBinding.etInputValueTop.text.isNullOrEmpty()) {
                val valueInput = mPlcBinding.etInputValueTop.text.toString().trim().toDouble()
                mPlcBinding.tvInputValueBottom.text =
                    ToolsSpeedConverterUtils.convert(valueInput, mTopUnitData, mBottomUnitData)
            }
        } catch (_: Exception) {

        }
    }

    override fun getLayoutValue(): ActivityToolsSpeedConvertPlfBinding {
        return ActivityToolsSpeedConvertPlfBinding.inflate(layoutInflater)
    }

}