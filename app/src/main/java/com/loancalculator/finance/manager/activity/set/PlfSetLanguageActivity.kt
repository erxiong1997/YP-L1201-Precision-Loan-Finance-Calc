package com.loancalculator.finance.manager.activity.set

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.language.MultiLanguages
import com.loancalculator.finance.manager.R
import com.loancalculator.finance.manager.StartPlfActivity
import com.loancalculator.finance.manager.activity.PlfBindingActivity
import com.loancalculator.finance.manager.adapter.AdapterLanguageItemPlf
import com.loancalculator.finance.manager.data.DataPlfLanguage
import com.loancalculator.finance.manager.databinding.ActivitySetLanguagePlfBinding
import java.util.Locale

class PlfSetLanguageActivity : PlfBindingActivity<ActivitySetLanguagePlfBinding>() {
    private lateinit var mAdapterLanguageItemPlf: AdapterLanguageItemPlf
    private val mListDoData = mutableListOf<DataPlfLanguage>()
    private var mPlfSelectIndex = -1

    override fun beginViewAndDoPlf() {
        mStarNativeValue = true

        mPlcBinding.topSetPlf.tvTitleAll.text = getString(R.string.plf_language)
        setPlfRecyclerView()
        mPlcBinding.topSetPlf.ivSelect.setOnClickListener {
            for (data in mListDoData) {
                if (data.fingerSelect) {
                    MultiLanguages.setAppLanguage(this, data.mLocale)
                    startActivity(Intent(this, StartPlfActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })

                    finish()
                    break
                }
            }
        }
    }

    override fun setPlfRecyclerView() {
        initLanguageDataPlf()
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
            mPlcBinding.rvRvView.smoothScrollToPosition(mPlfSelectIndex + 3)
        }, 636)
    }

    private fun initLanguageDataPlf() {

        mListDoData.add(DataPlfLanguage(getString(R.string.plf_en), Locale("en", "US")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_ar), Locale("ar", "AE")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_bn), Locale("bn", "BD")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_es), Locale("es", "US")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_pt), Locale("pt", "BR")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_fr), Locale("fr", "FR")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_in), Locale("in", "ID")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_tr), Locale("tr", "TR")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_de), Locale("de", "DE")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_th), Locale("th", "TH")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_it), Locale("it", "IT")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_vi), Locale("vi", "VN")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_hi), Locale("hi", "IN")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_fil), Locale("fil", "PH")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_ko), Locale("ko", "KR")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_ja), Locale("ja", "JP")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_ru), Locale("ru", "RU")))
        mListDoData.add(DataPlfLanguage(getString(R.string.plf_ms), Locale("ms", "MY")))

        val locale = MultiLanguages.getAppLanguage(this)
        for (index in mListDoData.indices) {
            val data = mListDoData[index]
            if (data.mLocale.language == locale.language) {
                data.fingerSelect = true
                mPlfSelectIndex = index
                break
            }
        }
        if (mPlfSelectIndex == -1) {
            mPlfSelectIndex = 0
            mListDoData[0].fingerSelect = true
        }
    }

    override fun getLayoutValue(): ActivitySetLanguagePlfBinding {
        return ActivitySetLanguagePlfBinding.inflate(layoutInflater)
    }
}