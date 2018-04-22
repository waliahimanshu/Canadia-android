package com.waliahimanshu.canadia.ui.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.waliahimanshu.canadia.ui.R
import kotlinx.android.synthetic.main.cic_ee_rounds_item_list_content.view.*

class ExpressEntryAdapter :
        RecyclerView.Adapter<ExpressEntryAdapter.ViewHolder> {

    private val parentActivity: ExpressEntryActivity
    private val twoPane: Boolean
    private val itemList: ArrayList<ExpressEntryModel>

    constructor(parentActivity: ExpressEntryActivity, twoPane: Boolean, itemList: ArrayList<ExpressEntryModel>) : super() {
        this.parentActivity = parentActivity
        this.twoPane = twoPane
        this.itemList = itemList
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cic_ee_rounds_item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.crsDrawDate.text = item.crsDrawDate.substringBefore("at").trim()
        holder.crsValue.text = item.crsScore
//        holder.numberOfIta.text = item.totalItaIssued
//        holder.tieBreakerDate.text = item.tieBreakerDate

        with(holder.itemView) {
            tag = item
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val crsDrawDate: TextView = mView.crs_draw_date
            val crsValue: TextView = mView.crs_value
//        val numberOfIta: TextView = mView.ita_issued
//        val tieBreakerDate: TextView = mView.tie_braking_date
    }
}