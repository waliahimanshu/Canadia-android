package com.waliahimanshu.canadia.ui.home

import android.support.v7.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

interface ExpressEntryModel {
    fun id(): Int
    fun getItemViewType(): Int
    fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder)
}

class MonthModel(val month: String) : ExpressEntryModel {
    override fun getItemViewType(): Int {
        return id()
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val textViewHolder = viewHolder as ViewHolderFactory.DateViewHolder
//        textViewHolder.month.text = month
    }

    companion object {
        const val DATE_HEADER_VIEW = 0
    }

    override fun id(): Int {
        return DATE_HEADER_VIEW
    }
}

class DataItemsModel(val year: String, val date: Date?, val crsScore: String, val tieBreakerDate: String,
                     val totalItaIssued: String) : ExpressEntryModel {
    override fun getItemViewType(): Int {
        return id()
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder) {
        val textViewHolder: ViewHolderFactory.ItemViewHolder = viewHolder as ViewHolderFactory.ItemViewHolder
        textViewHolder.crsDrawDate.text =  SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH).format(date)
        textViewHolder.crsValue.text = crsScore
        textViewHolder.numberOfIta.text = totalItaIssued
    }

    companion object {
        const val CONTENT_ITEM_VIEW = 1
    }

    override fun id(): Int {
        return CONTENT_ITEM_VIEW
    }
}
