package com.waliahimanshu.canadia.ui.home

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject


class ExpressEntryPresenter @Inject constructor(private val mainView: ExpressEntryContract.View,
                                                private val databaseReference: DatabaseReference,
                                                mapper: ExpressEntryMapper) : ExpressEntryContract.Presenter {

    override fun stop() {

    }

    override fun start() {
        loadData()
    }


    private fun loadData(): ArrayList<ExpressEntryModel> {
        val itemList: ArrayList<ExpressEntryModel> = java.util.ArrayList()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val children = dataSnapshot.children

                for (item: DataSnapshot in children) {

                    val invitationModel: ExpressEntryModel? = item.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        itemList.add(invitationModel)
                    }
                }
                if (itemList.isNotEmpty()) {
                    handleSuccess(itemList)

                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                TODO("logging db fetch failed")
            }
        })
        return itemList
    }

    private fun handleSuccess(itemList: ArrayList<ExpressEntryModel>) {
        mainView.showProgressBar(false)
        mainView.showData(itemList)
    }
}
