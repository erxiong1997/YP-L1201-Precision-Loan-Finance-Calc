package com.loancalculator.finance.manager.activity.set

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.language.MultiLanguages
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.StartPlfActivity
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterLanguageItemPlf
import com.loancalculator.finance.manager.data.DataLtdLanguage
import com.loancalculator.finance.manager.databinding.ActivitySetLanguagePlfBinding

class PlfSetLanguageActivity : PlfBindingActivity<ActivitySetLanguagePlfBinding>() {
    private lateinit var mAdapterLanguageItemPlf: AdapterLanguageItemPlf
    private val mListDoData = mutableListOf<DataLtdLanguage>()
    private var mLtdSelectIndex = -1

//    override fun doBackPressed() {
//        showBookAdTil("ltdBack") {
//            super.doBackPressed()
//        }
//    }

    override fun beginViewAndDoLtd() {
        mStarNativeValue = true

        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_language)
        setLtdRecyclerView()
//        mPlcBinding.topSetPlf.ivChange.setOnClickListener {
//            for (data in mListDoData) {
//                if (data.fingerSelect) {
//                    MultiLanguages.setAppLanguage(this, data.mLocale)
//                    startActivity(Intent(this, StartPlfActivity::class.java).apply {
//                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    })
//                    finish()
//                    break
//                }
//            }
//        }
    }

    override fun setLtdRecyclerView() {
        initLanguageDataLtd()
        mAdapterLanguageItemPlf = AdapterLanguageItemPlf(this, mListDoData) {
            val data = mListDoData[it]
            if (data.fingerSelect) return@AdapterLanguageItemPlf
            for (i in mListDoData.indices) {
                if (mListDoData[i].fingerSelect) {
                    mListDoData[i].fingerSelect = false
                    mAdapterLanguageItemPlf.notifyItemChanged(i)
                    break
                }
            }
            data.fingerSelect = true
            mAdapterLanguageItemPlf.notifyItemChanged(it)
        }
        mPlcBinding.rvRvView.layoutManager = LinearLayoutManager(this)
        mPlcBinding.rvRvView.adapter = mAdapterLanguageItemPlf
        mPlcBinding.rvRvView.postDelayed({
            mPlcBinding.rvRvView.smoothScrollToPosition(mLtdSelectIndex + 3)
        }, 800)
    }

    private fun initLanguageDataLtd() {
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_en), Locale("en", "US")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_es), Locale("es", "US")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_pt), Locale("pt", "BR")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_fr), Locale("fr", "FR")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_in), Locale("in", "ID")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_tr), Locale("tr", "TR")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_ar), Locale("ar", "AE")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_de), Locale("de", "DE")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_th), Locale("th", "TH")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_it), Locale("it", "IT")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_vi), Locale("vi", "VN")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_hi), Locale("hi", "IN")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_fil), Locale("fil", "PH")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_ko), Locale("ko", "KR")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_ja), Locale("ja", "JP")))
//        mListDoData.add(DataLtdLanguage(getString(R.string.ltd_bn), Locale("bn", "BD")))

        val locale = MultiLanguages.getAppLanguage(this)
        for (index in mListDoData.indices) {
            val data = mListDoData[index]
            if (data.mLocale.language == locale.language) {
                data.fingerSelect = true
                mLtdSelectIndex = index
                break
            }
        }
        if (mLtdSelectIndex == -1) {
            mLtdSelectIndex = 0
            mListDoData[0].fingerSelect = true
        }
    }

    override fun getLayoutValue(): ActivitySetLanguagePlfBinding {
        return ActivitySetLanguagePlfBinding.inflate(layoutInflater)
    }
}