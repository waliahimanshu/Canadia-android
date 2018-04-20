package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import javax.inject.Inject




class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val eeCrsReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    var originalList: ArrayList<ExpressEntryModel> = arrayListOf()
    private var filterCopyList: MutableList<ExpressEntryModel> = arrayListOf()


    private var childEventListener: ChildEventListener = object : ChildEventListener {
        override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {

            val invitationModel: ExpressEntryModel? = dataSnapshot.getValue(ExpressEntryModel::class.java)

            if (invitationModel != null) {
                invitationModel.year = dataSnapshot.key
                originalList.add(invitationModel)

            }
            if (originalList.isEmpty()) {
                mainView.showEmptyState()
            } else {
                filterCopyList.addAll(originalList)
                mainView.showProgressBar(false)
                mainView.showData(originalList)
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

    override fun addDataFor(year: String) {

        val listToBeAdded = arrayListOf<ExpressEntryModel>()
        for (expressEntryModel in originalList) {
            if(expressEntryModel.year == year){
                listToBeAdded.add(expressEntryModel)
            }
        }
        filterCopyList.addAll(listToBeAdded)
        mainView.addData(filterCopyList)
    }


    override fun removeDataFor(year: String) {
//        filterListMap.remove(year)
//        mainView.showData(filterListMap)

         filterCopyList.removeAll { f -> f.year == year }
        mainView.removeData(filterCopyList)
    }


    override fun stop() {

    }

    override fun start() {
        if (filterCopyList.isEmpty()) {
            loadData()
        }
    }


    private fun loadData() {
        eeCrsReference.orderByKey()
//        eeCrsReference.keepSynced(true)
        eeCrsReference.child("2017").addChildEventListener(childEventListener)
    }

    override fun onDestroy() {
        eeCrsReference.removeEventListener(childEventListener)
    }
}
