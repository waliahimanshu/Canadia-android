package com.waliahimanshu.canadia.ui.home

import android.support.v7.util.DiffUtil

class MyDiffCallback(private var newExpressEntryData: List<ExpressEntryDTO>,
                     private var oldExpressEntryData: List<ExpressEntryDTO>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldExpressEntryData.size

    }

    override fun getNewListSize(): Int {
        return newExpressEntryData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldExpressEntryData[oldItemPosition].crsDrawDate == newExpressEntryData[newItemPosition].crsDrawDate
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldExpressEntryData[oldItemPosition] == newExpressEntryData[newItemPosition]
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}