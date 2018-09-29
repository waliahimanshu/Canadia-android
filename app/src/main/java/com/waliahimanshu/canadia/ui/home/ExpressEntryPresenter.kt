package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.*
import java.util.*
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val eeCrsReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {


    private val singleValueEventListener: ValueEventListener = object : ValueEventListener {
        lateinit var allItemList: ArrayList<ExpressEntryDTO>
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val child = dataSnapshot.child("2018").children
            allItemList = ArrayList()
            for (element: DataSnapshot in child) {
                val invitationDTO: ExpressEntryDTO? = element.getValue(ExpressEntryDTO::class.java)
                if (invitationDTO != null) {
                    allItemList.add(invitationDTO)
                }
            }
            if (allItemList.isEmpty()) {
                mainView.showEmptyState()
            } else {
                mainView.showProgressBar(false)
                val model = mapper.map(allItemList)

                mainView.loadInitCurrentYearData(model)
            }
        }

        override fun onCancelled(dataSnapshot: DatabaseError) {
            val message = dataSnapshot.message
            mainView.handleDatabaseLoadError(message)
        }
    }

    override fun loadData() {
        start()
    }

    private var childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
            val invitationDTO: ExpressEntryDTO? = dataSnapshot.getValue(ExpressEntryDTO::class.java)

            // onChild added is called for each child first time the app runs and after when the child is
            // added
            if (invitationDTO != null) {
                // new item added do stuff ??
            }
        }

        override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onChildRemoved(dataSnapshot: DataSnapshot) {
        }

        override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
        }

        override fun onCancelled(databaseError: DatabaseError) {
        }
    }

    override fun start() {
        eeCrsReference.orderByKey()
//        eeCrsReference.child("2018").addChildEventListener(childEventListener)
        eeCrsReference.addListenerForSingleValueEvent(singleValueEventListener)
    }

    override fun stop() {

    }

    override fun onDestroy() {
        eeCrsReference.removeEventListener(childEventListener)
    }
}
