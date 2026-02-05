package com.loancalculator.finance.manager.utils.value

import androidx.annotation.StringDef

class ConstantObjectManagerUtils {
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(
    LoanTypePlf.PERSONAL,
    LoanTypePlf.BUSINESS,
    LoanTypePlf.MORTGAGES,
    LoanTypePlf.AUTO,
    LoanTypePlf.RD,
    LoanTypePlf.FD
)
annotation class LoanTypePlfDef

object LoanTypePlf {
    const val PERSONAL = "personalLoan"
    const val BUSINESS = "businessLoan"
    const val MORTGAGES = "mortgages"
    const val AUTO = "autoLoan"
    const val RD = "fd"
    const val FD = "rd"
}