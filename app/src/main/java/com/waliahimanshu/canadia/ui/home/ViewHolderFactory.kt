package com.waliahimanshu.canadia.ui.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.waliahimanshu.canadia.ui.R
import kotlinx.android.synthetic.main.ee_item_list_content.view.*


class ViewHolderFactory {

    companion object {
        fun create(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            when (viewType) {
                MonthModel.DATE_HEADER_VIEW -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.ee_header, parent, false)
                    return ViewHolderFactory.DateViewHolder(view)
                }
                DataItemsModel.CONTENT_ITEM_VIEW -> {
                    val view = LayoutInflater.from(parent.context).inflate(R.layout.ee_item_list_content, parent, false)
                    return ViewHolderFactory.ItemViewHolder(view)
                }
            }
            throw IllegalArgumentException("Invalid View type $viewType")
        }
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val crsDrawDate: TextView = view.crs_draw_date
        val crsValue: TextView = view.crs_value
        val numberOfIta: TextView = view.ita_issued
    }

    class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val month: TextView = view.month
    }
}
