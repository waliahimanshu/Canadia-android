package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.util.BasePresenter
import com.waliahimanshu.canadia.util.BaseView

interface ExpressEntryContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(show: Boolean)
        fun showData(itemList: ArrayList<ExpressEntryModel>)
    }

    interface Presenter : BasePresenter {

    }

}
