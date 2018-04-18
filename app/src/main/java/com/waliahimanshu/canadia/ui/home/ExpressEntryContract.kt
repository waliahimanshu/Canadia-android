package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.util.BasePresenter
import com.waliahimanshu.canadia.util.BaseView

interface ExpressEntryContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(show: Boolean)
        fun handleDatabaseLoadError(message: String?)
        fun showEmptyState()
        fun showData(itemList: ArrayList<ExpressEntryModel>)
        fun removeData(filterList: MutableList<ExpressEntryModel>)
        fun addData(filterCopyList: MutableList<ExpressEntryModel>)
    }

    interface Presenter : BasePresenter {
        fun addDataFor(year: String)
        fun removeDataFor(year: String)
        fun onDestroy()
    }

}
