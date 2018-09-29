package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.util.BasePresenter
import com.waliahimanshu.canadia.util.BaseView

interface ExpressEntryContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(show: Boolean)
        fun handleDatabaseLoadError(message: String?)
        fun showEmptyState()
        fun loadInitCurrentYearData(dataSet: ArrayList<ExpressEntryModel>)
        fun setToolbarTitle(year: String)
    }

    interface Presenter : BasePresenter {
        fun onDestroy()
        fun loadData()
    }
}
