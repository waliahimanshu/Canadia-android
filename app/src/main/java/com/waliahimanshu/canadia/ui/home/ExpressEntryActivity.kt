package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.CheckedTextView
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.waliahimanshu.canadia.ui.R
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.menu_filter_layout.*
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

        setUpFilters()
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
        expressEntryAdapter.showData(itemList)
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

    override fun handleDatabaseLoadError(message: String?) {
        Toast.makeText(baseContext, message, LENGTH_LONG).show()
    }

    override fun showEmptyState() {
        recyler_view.visibility = GONE

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

    private fun setUpFilters() {
        all_years.setOnClickListener {
            if (all_years.isChecked) {
                disable(all_years)

            } else {
                enabled(all_years)
                disable(year_2018)
                disable(year_2017)
                disable(year_2016)
                disable(year_2015)
            }
        }

        year_2018.setOnClickListener {
            if (year_2018.isChecked) {
                disable(year_2018)
                expressEntryPresenter.loadDataFor("2018")

            } else {
                enabled(year_2018)
                disable(all_years)
            }
        }

        year_2017.setOnClickListener {
            if (year_2017.isChecked) {
                disable(year_2017)

            } else {
                enabled(year_2017)
                disable(all_years)
            }
        }

        year_2016.setOnClickListener {
            if (year_2016.isChecked) {
                disable(year_2016)

            } else {
                enabled(year_2016)
                disable(all_years)

            }
        }

        year_2015.setOnClickListener {
            if (year_2015.isChecked) {
                disable(year_2015)

            } else {
                enabled(year_2015)
                disable(all_years)
            }
        }
    }

    private fun enabled(checkedTextView: CheckedTextView) {
        checkedTextView.isChecked = true
        checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_black_24dp)
        checkedTextView.setTextColor(resources.getColor(R.color.red_600))
    }

    private fun disable(checkedTextView: CheckedTextView) {
        checkedTextView.isChecked = false
        checkedTextView.setCheckMarkDrawable(R.drawable.ic_check_transparent_24dp)
        checkedTextView.setTextColor(resources.getColor(R.color.grey_500))
    }
}