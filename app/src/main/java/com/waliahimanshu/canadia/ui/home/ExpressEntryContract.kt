package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.util.BasePresenter
import com.waliahimanshu.canadia.util.BaseView
import java.util.*

interface ExpressEntryContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(show: Boolean)
        fun showData(itemList: ArrayList<ExpressEntryModel>)
        fun handleDatabaseLoadError(message: String?)
        fun showEmptyState()
    }

    interface Presenter : BasePresenter {
        fun loadDataFor(year: String)
    }

}
