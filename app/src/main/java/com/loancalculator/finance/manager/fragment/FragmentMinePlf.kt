package com.loancalculator.finance.manager.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.loancalculator.finance.manager.activity.PlfRootActivity
import com.loancalculator.finance.manager.activity.other.PlfStartHelpActivity
import com.loancalculator.finance.manager.activity.set.PlfSetLanguageActivity
import com.loancalculator.finance.manager.activity.set.PlfSetPolicyActivity
import com.loancalculator.finance.manager.databinding.FragmentMinePlfBinding
import com.loancalculator.finance.manager.setSafeListener
import com.loancalculator.finance.manager.utils.PlfTotalUtils

class FragmentMinePlf : RootPlfFragment<FragmentMinePlfBinding>() {
    companion object {
        private const val TIL_PAGE = "til_page"

        fun newInstance(page: Int): FragmentMinePlf {
            val fragment = FragmentMinePlf()
            fragment.arguments = Bundle().apply {
                putInt(TIL_PAGE, page)
            }
            return fragment
        }
    }

    override fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    ) {
        mPlfBinding.llPrivacyPolicy.setSafeListener {
            startActivity(Intent(rootActivity, PlfSetPolicyActivity::class.java))
        }
        mPlfBinding.llLanguage.setSafeListener {
//            startActivity(Intent(rootActivity, PlfSetLanguageActivity::class.java))
            startActivity(Intent(rootActivity, PlfStartHelpActivity::class.java))
        }
        mPlfBinding.llShare.setSafeListener {
            PlfTotalUtils.shareAppLinkPlf(rootActivity)
        }
    }

    override fun getLayoutValue(): FragmentMinePlfBinding {
        return FragmentMinePlfBinding.inflate(layoutInflater)
    }
}