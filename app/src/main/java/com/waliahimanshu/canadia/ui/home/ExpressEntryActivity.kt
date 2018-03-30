package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.RadioButton
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.waliahimanshu.canadia.ui.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import javax.inject.Inject

class ExpressEntryActivity : AppCompatActivity(), ExpressEntryContract.View {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var database: DatabaseReference
    private lateinit var expressEntryAdapter: ExpressEntryAdapter

    @Inject
    lateinit var expressEntryPresenter: ExpressEntryContract.Presenter

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, ExpressEntryActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)
        AndroidInjection.inject(this)
        setSupportActionBar(toolbar)

        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        getFilters()
        setupRecyclerView()
    }

    override fun setPresenter(presenter: ExpressEntryContract.Presenter) {
        expressEntryPresenter = presenter
    }

    override fun onStart() {
        super.onStart()
        expressEntryPresenter.start()
    }

    override fun showData(itemList: ArrayList<ExpressEntryModel>) {
        expressEntryAdapter.expressEntryModels = itemList
        expressEntryAdapter.notifyDataSetChanged()
    }

    override fun showProgressBar(show: Boolean) {
        progressBar?.visibility = View.GONE

    }

    private fun loadDataByCRS(string: String): ArrayList<ExpressEntryModel> {
        val itemList: ArrayList<ExpressEntryModel> = java.util.ArrayList()

        database = database.child("ee_crs").child("2018")
        val queryRef = database.orderByChild("crs_score")


        queryRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (item: DataSnapshot in dataSnapshot.children) {
                    val invitationModel: ExpressEntryModel? = item.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        itemList.add(invitationModel)
                    }
                }
                if (itemList.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                TODO("logging db fetch failed")
            }
        })
        return itemList
    }

    private fun setupRecyclerView() {
        expressEntryAdapter = ExpressEntryAdapter(this, twoPane)
        recyler_view.adapter = expressEntryAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                drawerLayout.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getFilters() {
        val checkBoxAll = findViewById<CheckBox>(R.id.all_year)
        val checkBox2018 = findViewById<CheckBox>(R.id.year_2018)
        val checkBox2017 = findViewById<CheckBox>(R.id.year_2017)
        val lowestCRS = findViewById<RadioButton>(R.id.lowestCRS)
        val highyestCRS = findViewById<RadioButton>(R.id.highestCRS)

        checkBoxAll.isChecked = true

        checkBox2018.setOnCheckedChangeListener { button, isChecked ->

            }

        checkBox2017.setOnCheckedChangeListener { button, isChecked ->

        }

        highyestCRS.setOnCheckedChangeListener { button, isSelected ->
            if (isSelected) {
                loadDataByCRS("2018")
            } else {

            }
        }
    }

}