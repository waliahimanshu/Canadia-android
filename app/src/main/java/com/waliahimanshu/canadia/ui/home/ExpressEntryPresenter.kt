package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import java.util.*
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val view: ExpressEntryContract.View,
                                                private val eeCrsReference: DatabaseReference) : ExpressEntryContract.Presenter {


    private val singleValueEventListener: ValueEventListener = object : ValueEventListener {

        lateinit var allItemList: ArrayList<ExpressEntryDTO>
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val year = Calendar.getInstance().get(Calendar.YEAR).toString()
            val child = dataSnapshot.child(year).children
            allItemList = ArrayList()
            for (element: DataSnapshot in child) {
                val invitationDTO: ExpressEntryDTO? = element.getValue(ExpressEntryDTO::class.java)
                if (invitationDTO != null) {
                    allItemList.add(invitationDTO)
                }
            }
            if (allItemList.isEmpty()) {
                view.showEmptyState()
            } else {
                view.showProgressBar(false)
                view.loadInitCurrentYearData(allItemList)
                view.setToolbarTitle(year)
            }
        }

        override fun onCancelled(dataSnapshot: DatabaseError) {
            val message = dataSnapshot.message
            view.handleDatabaseLoadError(message)
        }
    }

    override fun loadData() {
        start()
    }

    override fun start() {
        eeCrsReference.orderByKey()
        eeCrsReference.addListenerForSingleValueEvent(singleValueEventListener)
    }

    override fun stop() {

    }

    override fun onDestroy() {
        eeCrsReference.removeEventListener(singleValueEventListener)
    }
}
