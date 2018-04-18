package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val databaseReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    var originalList: ArrayList<ExpressEntryModel> = arrayListOf()
    private var filterCopyList: MutableList<ExpressEntryModel> = arrayListOf()

    private val value: ValueEventListener = object : ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            val child = dataSnapshot.children
            for (item: DataSnapshot in child) {
                for (element: DataSnapshot in item.children) {
                    val invitationModel: ExpressEntryModel? = element.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        invitationModel.year = item.key
                        originalList.add(invitationModel)
                    }
                }
            }
            if (originalList.isEmpty()) {
                mainView.showEmptyState()
            } else {
                filterCopyList.addAll(originalList)
                mainView.showProgressBar(false)
                mainView.showData(originalList)
            }
        }

        override fun onCancelled(dataSnapshot: DatabaseError?) {
            val message = dataSnapshot?.message
            mainView.handleDatabaseLoadError(message)
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
        databaseReference.orderByKey()
        databaseReference.addListenerForSingleValueEvent(value)
    }

    override fun onDestroy() {
        databaseReference.removeEventListener(value)
    }

}
