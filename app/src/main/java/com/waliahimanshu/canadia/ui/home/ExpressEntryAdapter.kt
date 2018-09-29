package com.waliahimanshu.canadia.ui.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.waliahimanshu.canadia.ui.R


class ExpressEntryAdapter(private val parentActivity: ExpressEntryActivity,
                          private val twoPane: Boolean,
                          private val dataSet: ArrayList<ExpressEntryModel>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val onItemClickListener = View.OnClickListener { view ->
        val item = view.tag as ExpressEntryModel
        if (this.twoPane) {
            val arguments = Bundle()
//            arguments.putString(ItemDetailFragment.ARG_ITEM_ID, item.year)
            val fragment = ItemDetailFragment()
            fragment.arguments = arguments
            parentActivity.supportFragmentManager.beginTransaction().replace(R.id.item_detail_container,
                    fragment).commit()
        } else {
            val context = view.context
            val intent = Intent(context, ItemDetailActivity::class.java)
//            intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, item.year)

            context.startActivity(intent)
        }
    }


    override fun getItemViewType(position: Int): Int {
        return dataSet[position].getItemViewType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderFactory.create(parent, viewType)
    }

    override fun onBindViewHolder(holderItem: RecyclerView.ViewHolder, position: Int) {
        var model: Unit? = null

            model = dataSet[position].onBindViewHolder(holderItem)


//        if (model is MonthModel) {
//            val dateViewHolder = holderItem as DateViewHolder
//            dateViewHolder.month.text = model.month
//
//        }
//
//        if (model is DataItemsModel) {
//            val itemViewHolder = holderItem as ItemViewHolder
//
//            itemViewHolder.crsValue.text = model.crsScore
//            itemViewHolder.crsDrawDate.text = model.date.toString()
////            itemViewHolder.numberOfIta.text = model.totalItaIssued
////           itemViewHolder.tieBreakerDate.text = model.tieBreakerDate
//        }

        with(holderItem.itemView) {
            tag = model
            setOnClickListener(onItemClickListener)

        }

    }

    override fun getItemCount(): Int {
        return dataSet.size
    }


}
