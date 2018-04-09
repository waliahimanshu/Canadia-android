package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val databaseReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    val hashMap: LinkedHashMap<String, ArrayList<ExpressEntryModel>> = LinkedHashMap()
    private var filterListMap: LinkedHashMap<String, ArrayList<ExpressEntryModel>> = LinkedHashMap()

    private val value: ValueEventListener = object : ValueEventListener {
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
                hashMap[item.key] = allItemList
            }
            if (allItemList.isEmpty()) {
                mainView.showEmptyState()
            } else {
                filterListMap.putAll(hashMap)
                handleSuccess(hashMap)
            }
        }

        override fun onCancelled(dataSnapshot: DatabaseError?) {
            val message = dataSnapshot?.message
            mainView.handleDatabaseLoadError(message)
        }
    }

    override fun loadDataFor(year: String) {
        val value = hashMap[year]

        if (value != null) {
            filterListMap[year] = value
        }
        mainView.showData(filterListMap)
    }


    override fun removeDataFor(year: String) {
        filterListMap.remove(year)
        mainView.showData(filterListMap)
    }


    override fun stop() {

    }

    override fun start() {
        loadData()
    }


    private fun loadData() {
        if (!hashMap.isEmpty()) {
            handleSuccess(hashMap)
        }
        databaseReference.orderByKey()
        databaseReference.addListenerForSingleValueEvent(value)
    }

    override fun onDestroy() {
        databaseReference.removeEventListener(value)
    }

    private fun handleSuccess(itemHashMap: HashMap<String, ArrayList<ExpressEntryModel>>) {
        mainView.showProgressBar(false)
        mainView.showData(itemHashMap)
    }
}
