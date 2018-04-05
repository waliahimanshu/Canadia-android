package com.waliahimanshu.canadia.ui.home

import com.waliahimanshu.canadia.util.BasePresenter
import com.waliahimanshu.canadia.util.BaseView

interface ExpressEntryContract {
    interface View : BaseView<Presenter> {
        fun showProgressBar(show: Boolean)
        fun showData(itemList: HashMap<String, ArrayList<ExpressEntryModel>>)
        fun handleDatabaseLoadError(message: String?)
        fun showEmptyState()
    }

    interface Presenter : BasePresenter {
        fun loadDataFor(year: String)
        fun removeDataFor(year: String)
        fun onDestroy()
    }

}
