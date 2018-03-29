package com.waliahimanshu.canadia.ui.home

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.waliahimanshu.canadia.ui.R
import kotlinx.android.synthetic.main.cic_ee_rounds_item_list_content.view.*

class RoundOfInvitationsAdapter(private val parentActivity: EERoundOfInvitationsActivity,
                                private val values: List<RoundOfInvitationModel>,
                                private val twoPane: Boolean) :
        RecyclerView.Adapter<RoundOfInvitationsAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as RoundOfInvitationModel
            if (twoPane) {
                val fragment = ItemDetailFragment().apply {
//                    arguments = Bundle(ItemDetailFragment).apply {
//                        putInt(ItemDetailFragment.ARG_ITEM_ID, 1)
//                    }
                }
                parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.item_detail_container, fragment)
                        .commit()
            } else {
                val intent = Intent(v.context, ItemDetailActivity::class.java).apply {
                    putExtra(ItemDetailFragment.ARG_ITEM_ID, /*item.id*/1)
                }
                v.context.startActivity(intent)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.cic_ee_rounds_item_list_content, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.crsDrawDate.text = item.crsDrawDate
        holder.crsValue.text = item.crsScore
        holder.numberOfIta.text = item.totalItaIssued
        holder.tieBreakerDate.text = item.tieBreakerDate

        with(holder.itemView) {
            tag = item
//            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int {
        return values.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val crsDrawDate: TextView = mView.crs_draw_date
        val crsValue: TextView = mView.crs_value
        val numberOfIta: TextView = mView.ita_issued
        val tieBreakerDate: TextView = mView.tie_braking_date
    }
}