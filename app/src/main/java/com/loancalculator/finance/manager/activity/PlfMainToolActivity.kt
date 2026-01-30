package com.loancalculator.finance.manager.activity

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.loancalculator.finance.manager.databinding.ActivityMainPlfBinding
import com.loancalculator.finance.manager.fragment.FragmentComparePlf
import com.loancalculator.finance.manager.fragment.FragmentHomePlf
import com.loancalculator.finance.manager.fragment.FragmentMinePlf
import com.loancalculator.finance.manager.fragment.FragmentToolsPlf
import com.loancalculator.finance.manager.fragment.RootPlfFragment
import com.loancalculator.finance.manager.setSafeListener

class PlfMainToolActivity : PlfBindingActivity<ActivityMainPlfBinding>() {
    private val mFragmentList = mutableListOf<RootPlfFragment<*>>()
    private val mFragmentValueList = mutableListOf(1, 2, 3, 4)

    override fun beginViewAndDoLtd() {
//        mPlcBinding.tvLoanTwo.setSafeListener {
//            startActivity(Intent(this, PlfToolsTemperatureActivity::class.java))
//        }
//        mPlcBinding.tvLoanThree.setSafeListener {
//            startActivity(Intent(this, PlfToolsWorldTimeActivity::class.java))
//        }

        addViewPage2()
        mPlcBinding.tvTbHome.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(0, true)
        }
        mPlcBinding.tvTbTools.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(1, true)
        }
        mPlcBinding.tvTbCompare.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(2, true)
        }
        mPlcBinding.tvTbMine.setSafeListener {
            if (it.isSelected) return@setSafeListener
            movePosition(3, true)
        }
    }

    private val mListenerPager = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            movePosition(position, false)
        }
    }

    private fun addViewPage2() {
        mFragmentList.add(FragmentHomePlf.newInstance(mFragmentValueList[0]))
        mFragmentList.add(FragmentToolsPlf.newInstance(mFragmentValueList[1]))
        mFragmentList.add(FragmentComparePlf.newInstance(mFragmentValueList[2]))
        mFragmentList.add(FragmentMinePlf.newInstance(mFragmentValueList[3]))

        mPlcBinding.tilViewPager.offscreenPageLimit = 4
        (mPlcBinding.tilViewPager.getChildAt(0) as? RecyclerView)?.overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER
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
        mPlcBinding.tvTbHome.isSelected = position == 0
        mPlcBinding.tvTbTools.isSelected = position == 1
        mPlcBinding.tvTbCompare.isSelected = position == 2
        mPlcBinding.tvTbMine.isSelected = position == 3
    }

    override fun getLayoutValue(): ActivityMainPlfBinding {
        return ActivityMainPlfBinding.inflate(layoutInflater)
    }
}