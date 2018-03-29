package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ProgressBar
import android.widget.RadioButton
import com.google.firebase.database.*
import com.waliahimanshu.canadia.ui.R
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*

class ExpressEntryActivity : AppCompatActivity(), ExpressEntryContract.View {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var database: DatabaseReference

    private var progressBar: ProgressBar? = null

    private var drawer: DrawerLayout? = null

    private var nvDrawer: NavigationView? = null

    companion object {
        fun getLaunchIntent(context: Context): Intent {
            return Intent(context, ExpressEntryActivity::class.java)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        setSupportActionBar(toolbar)
        toolbar.title = title


        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        database = FirebaseDatabase.getInstance().reference
        progressBar = findViewById(R.id.progress_bar_home)
        progressBar?.visibility = View.VISIBLE
        drawer = findViewById(R.id.drawer_layout)
        // Setup drawer view

        // Find our drawer view
        nvDrawer = findViewById(R.id.nvView)
        // Setup drawer view
        setupDrawerContent(nvDrawer)

        val checkBoxAll = findViewById<CheckBox>(R.id.all_year)
        val checkBox2018 = findViewById<CheckBox>(R.id.year_2018)
        val checkBox2017 = findViewById<CheckBox>(R.id.year_2017)
        val lowestCRS = findViewById<RadioButton>(R.id.lowestCRS)
        val highyestCRS = findViewById<RadioButton>(R.id.highestCRS)

        checkBoxAll.isChecked = true

        checkBox2018.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                loadData("2018")
            } else {
                loadData("2017")
            }
        }

        checkBox2017.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                loadData("2017")
            } else {
                loadData("2018")
            }
        }

        highyestCRS.setOnCheckedChangeListener { button, isSelected ->
            if (isSelected) {
                loadDataByCRS("2018")
            } else {

            }
        }

        loadData("2018")
    }


    private fun setupDrawerContent(navigationView: NavigationView?) {
        navigationView?.setNavigationItemSelectedListener { menuItem ->
            selectDrawerItem(menuItem)
            true
        }
    }

    private fun selectDrawerItem(menuItem: MenuItem) {
        menuItem.isChecked = true
        // Set action bar title
        title = menuItem.title
        // Close the navigation drawer
        drawer?.closeDrawer(GravityCompat.END)
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
                    setupRecyclerView(item_list, itemList)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                TODO("logging db fetch failed")
            }
        })
        return itemList
    }

    private fun loadData(string: String): ArrayList<ExpressEntryModel> {
        val itemList: ArrayList<ExpressEntryModel> = java.util.ArrayList()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {


                val eeCrsTable = dataSnapshot.child("ee_crs").child(string)
                val children = eeCrsTable.children

                for (item: DataSnapshot in children) {
                    val invitationModel: ExpressEntryModel? = item.getValue(ExpressEntryModel::class.java)
                    if (invitationModel != null) {
                        itemList.add(invitationModel)
                    }
                }
                if (itemList.isNotEmpty()) {
                    progressBar?.visibility = View.GONE
                    setupRecyclerView(item_list, itemList)
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError?) {
                TODO("logging db fetch failed")
            }
        })
        return itemList
    }


    private fun setupRecyclerView(recyclerView: RecyclerView, items: ArrayList<ExpressEntryModel>) {
        recyclerView.adapter = ExpressEntryAdapter(this, items, twoPane)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu_filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_filter -> {
                drawer?.openDrawer(GravityCompat.END)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}