package com.loancalculator.finance.manager.activity.compare

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.databinding.ActivityHistoryCalculatePlfBinding
import com.loancalculator.finance.manager.fragment.FragmentHistoryCalculatorPlf
import com.loancalculator.finance.manager.fragment.FragmentHistoryInvestmentPlf
import com.loancalculator.finance.manager.fragment.RootPlfFragment
import com.loancalculator.finance.manager.setSafeListener

class PlfHistoryCalculateActivity : PlfBindingActivity<ActivityHistoryCalculatePlfBinding>() {
    override fun doBackPressed() {
        if (mDeleteModel) {
            mPlcBinding.topSetPlf.ivDeleteHistory.visibility = View.VISIBLE
            mPlcBinding.topSetPlf.tvSelectAll.visibility = View.GONE
            mDeleteModel = false
            if (mPlcBinding.tilViewPager.currentItem == 0) {
                val fg1 = mFragmentList[0]
                (fg1 as? FragmentHistoryCalculatorPlf)?.initSelectStatus(mDeleteModel)
            }
            if (mPlcBinding.tilViewPager.currentItem == 1) {
                val fg1 = mFragmentList[1]
                (fg1 as? FragmentHistoryInvestmentPlf)?.initSelectStatus(mDeleteModel)
            }
        } else {
            super.doBackPressed()
        }
    }

    private val mFragmentList = mutableListOf<RootPlfFragment<*>>()
    private val mFragmentValueList = mutableListOf(1, 2)
    private var mDeleteModel = false
    override fun beginViewAndDoLtd() {
        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_history)
        mPlcBinding.tvCalculator.isSelected = true

        addViewPage2()

        mPlcBinding.tvCalculator.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(0, true)
        }
        mPlcBinding.tvInvestment.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(1, true)
        }

        mPlcBinding.topSetPlf.ivDeleteHistory.setSafeListener {
            it.visibility = View.INVISIBLE
            mPlcBinding.topSetPlf.tvSelectAll.visibility = View.VISIBLE
            mDeleteModel = true
            if (mPlcBinding.tilViewPager.currentItem == 0) {
                val fg1 = mFragmentList[0]
                (fg1 as? FragmentHistoryCalculatorPlf)?.initSelectStatus(mDeleteModel)
            }
            if (mPlcBinding.tilViewPager.currentItem == 1) {
                val fg1 = mFragmentList[1]
                (fg1 as? FragmentHistoryInvestmentPlf)?.initSelectStatus(mDeleteModel)
            }
        }
        mPlcBinding.topSetPlf.tvSelectAll.setSafeListener {
            it.isSelected = !it.isSelected
            if (mPlcBinding.tilViewPager.currentItem == 0) {
                val fg1 = mFragmentList[0]
                (fg1 as? FragmentHistoryCalculatorPlf)?.changeSelectStatus(it.isSelected)
            }
            if (mPlcBinding.tilViewPager.currentItem == 1) {
                val fg1 = mFragmentList[1]
                (fg1 as? FragmentHistoryInvestmentPlf)?.changeSelectStatus(it.isSelected)
            }
        }
    }

    private val mListenerPager = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            movePosition(position, false)
        }
    }

    private fun addViewPage2() {
        mFragmentList.add(FragmentHistoryCalculatorPlf.newInstance(mFragmentValueList[0]))
        mFragmentList.add(FragmentHistoryInvestmentPlf.newInstance(mFragmentValueList[1]))

        mPlcBinding.tilViewPager.offscreenPageLimit = 2
//        (mPlcBinding.tilViewPager.getChildAt(0) as? RecyclerView)?.overScrollMode =
//            RecyclerView.OVER_SCROLL_NEVER
        mPlcBinding.tilViewPager.adapter =
            object : FragmentStateAdapter(supportFragmentManager, lifecycle) {
                override fun getItemCount() = mFragmentList.size

                override fun createFragment(position: Int): Fragment {
                    return mFragmentList[position]
                }

                override fun getItemId(position: Int): Long {
                    return mFragmentValueList[position].hashCode().toLong()
                }

                override fun containsItem(itemId: Long): Boolean {
                    return mFragmentValueList.any { it.hashCode().toLong() == itemId }
                }
            }
        mPlcBinding.tilViewPager.registerOnPageChangeCallback(mListenerPager)
    }

    private fun movePosition(position: Int, changeItem: Boolean) {
        if (changeItem) {
            mPlcBinding.tilViewPager.currentItem = position
        }
        mPlcBinding.tvCalculator.isSelected = position == 0
        mPlcBinding.tvInvestment.isSelected = position == 1
        if (!changeItem) {
            val fg1 = mFragmentList[0]
            (fg1 as? FragmentHistoryCalculatorPlf)?.initSelectStatus(mDeleteModel)

            val fg2 = mFragmentList[1]
            (fg2 as? FragmentHistoryInvestmentPlf)?.initSelectStatus(mDeleteModel)
        }
    }

    override fun getLayoutValue(): ActivityHistoryCalculatePlfBinding {
        return ActivityHistoryCalculatePlfBinding.inflate(layoutInflater)
    }
}