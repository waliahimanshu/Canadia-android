package com.waliahimanshu.canadia.ui.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.waliahimanshu.canadia.ui.R
import kotlinx.android.synthetic.main.ee_item_list_content.view.*
import java.util.*


class ExpressEntryAdapter(private val dataSet: ArrayList<ExpressEntryDTO>) :
        RecyclerView.Adapter<ExpressEntryAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ee_item_list_content, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {

        itemViewHolder.crsValue.text = dataSet[position].crsScore
        itemViewHolder.crsDrawDate.text = dataSet[position].crsDrawDate
        itemViewHolder.numberOfIta.text = dataSet[position].totalItaIssued
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val crsDrawDate: TextView = view.crs_draw_date
        internal val crsValue: TextView = view.crs_value
        internal val numberOfIta: TextView = view.ita_issued
    }
}
