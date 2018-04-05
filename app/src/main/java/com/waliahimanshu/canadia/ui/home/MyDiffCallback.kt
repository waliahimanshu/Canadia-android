package com.waliahimanshu.canadia.ui.home

import android.support.v7.util.DiffUtil

class MyDiffCallback(private var newExpressEntryModel: List<ExpressEntryModel>,
                     private var oldExpressEntryModel: List<ExpressEntryModel>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldExpressEntryModel.size

    }

    override fun getNewListSize(): Int {
        return newExpressEntryModel.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldExpressEntryModel[oldItemPosition].crsDrawDate == newExpressEntryModel[newItemPosition].crsDrawDate
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldExpressEntryModel[oldItemPosition] == newExpressEntryModel[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}