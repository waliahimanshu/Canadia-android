package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.*
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val eeCrsReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    var currentYearList: ArrayList<ExpressEntryModel> = arrayListOf()
    val allYearMap: LinkedHashMap<String, ArrayList<ExpressEntryModel>> = LinkedHashMap()

    private val singleValueEventListener: ValueEventListener = object : ValueEventListener {
        lateinit var allItemList: ArrayList<ExpressEntryModel>
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val child = dataSnapshot.children
            for (item: DataSnapshot in child) {
                allItemList = ArrayList()
                for (element: DataSnapshot in item.children) {
                    val invitationModel: ExpressEntryModel? = element.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        allItemList.add(invitationModel)
                    }
                }
                allYearMap[item.key] = allItemList
            }
            if (allItemList.isEmpty()) {
                mainView.showEmptyState()
            }
        }

        override fun onCancelled(dataSnapshot: DatabaseError?) {
            val message = dataSnapshot?.message
            mainView.handleDatabaseLoadError(message)
        }
    }


    private var childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

//            if(allYearMap.get("2018")?.isEmpty()){  }


            val invitationModel: ExpressEntryModel? = dataSnapshot.getValue(ExpressEntryModel::class.java)

            if (invitationModel != null) {
                invitationModel.year = dataSnapshot.key
                currentYearList.add(invitationModel)
                mainView.showProgressBar(false)
                mainView.showData(currentYearList)
//                mainView.setToolbarTitle()

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
        eeCrsReference.child("2018").addChildEventListener(childEventListener)
        eeCrsReference.addListenerForSingleValueEvent(singleValueEventListener)
    }

    override fun showDataFor(year: String) {
        mainView.showProgressBar(false)
        val yearData = allYearMap[year]
        if (yearData != null) {
            mainView.showData(yearData)
            mainView.setToolbarTitle(year)
        }
    }

    override fun stop() {

    }


    override fun onDestroy() {
        eeCrsReference.removeEventListener(childEventListener)
    }
}
