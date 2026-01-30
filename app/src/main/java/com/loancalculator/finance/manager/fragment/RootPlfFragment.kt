package com.loancalculator.finance.manager.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.loancalculator.finance.manager.activity.PlfRootActivity

abstract class RootPlfFragment<VB : ViewBinding> : Fragment() {
    protected lateinit var mPlfBinding: VB

    protected fun isInitEnd() = ::mPlfBinding.isInitialized

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mPlfBinding = getLayoutValue()
        return mPlfBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity?.isFinishing == true || activity?.isDestroyed == true) return
        activity?.let {
            if (it is PlfRootActivity) {
                startCreateContent(it, view, savedInstanceState)
            }
        }
    }

    protected open fun setPlfRecyclerView(rootActivity: PlfRootActivity) {

    }

    protected abstract fun startCreateContent(
        rootActivity: PlfRootActivity,
        view: View,
        bundle: Bundle?
    )

    protected abstract fun getLayoutValue(): VB
}