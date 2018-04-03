package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val databaseReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    val hashMap: HashMap<String, ArrayList<ExpressEntryModel>> = HashMap()

    override fun loadDataFor(year: String) {

        val arrayList= hashMap[year]
        if (arrayList != null) {
            mainView.showData(arrayList)
        }
    }


    override fun stop() {

    }

    override fun start() {
        loadData()
    }


    private fun loadData() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
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
                    handleSuccess(hashMap)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                val message = dataSnapshot?.message
                mainView.handleDatabaseLoadError(message)
            }
        })
    }

    private fun handleSuccess(itemHashMap: HashMap<String, ArrayList<ExpressEntryModel>>) {
        mainView.showProgressBar(false)
        val allItemList: ArrayList<ExpressEntryModel> = arrayListOf()
        for (lis: List<ExpressEntryModel> in itemHashMap.values) {
            allItemList.addAll(lis)
        }
        mainView.showData(allItemList)
    }
}
