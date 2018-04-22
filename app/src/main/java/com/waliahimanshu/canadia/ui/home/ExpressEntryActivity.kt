package com.waliahimanshu.canadia.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.waliahimanshu.canadia.ui.R
import com.waliahimanshu.canadia.ui.login.LoginActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_item_detail.*
import kotlinx.android.synthetic.main.activity_item_list.*
import kotlinx.android.synthetic.main.item_list.*
import kotlinx.android.synthetic.main.menu_filter_layout.*
import javax.inject.Inject


class ExpressEntryActivity : AppCompatActivity(), ExpressEntryContract.View, GoogleApiClient.OnConnectionFailedListener {
    override fun setToolbarTitle(year: String) {
        toolbar.title = year
    }

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false
    private lateinit var database: DatabaseReference
    private lateinit var expressEntryAdapter: ExpressEntryAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var currentUser: FirebaseUser? = null
    private var googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()


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
        firebaseAuth = FirebaseAuth.getInstance()
        currentUser = firebaseAuth.currentUser

        googleApiAvailability.makeGooglePlayServicesAvailable(this)
        mGoogleApiClient = GoogleApiClient.Builder(baseContext)
                .enableAutoManage(this, this).addApi(Auth.GOOGLE_SIGN_IN_API).build()

        AndroidInjection.inject(this)
        setSupportActionBar(toolbar)


        if (item_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        sign_out.setOnClickListener {
            currentUser?.photoUrl
            currentUser?.displayName
            firebaseAuth.signOut()
            Auth.GoogleSignInApi.signOut(mGoogleApiClient)
            startActivity(LoginActivity.getLaunchIntent(this))
            Toast.makeText(this, "You are signed out from you account ${currentUser?.email} ", LENGTH_LONG).show()
            this.finish()
        }

        setUpFilters()
    }

    override fun setPresenter(presenter: ExpressEntryContract.Presenter) {
        expressEntryPresenter = presenter
    }

    override fun onDestroy() {
        super.onDestroy()
        expressEntryPresenter.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        googleApiAvailability.makeGooglePlayServicesAvailable(this)
    }

    override fun showData(itemList: ArrayList<ExpressEntryModel>) {
        if (itemList.isEmpty()) {
            showEmptyState()

        } else {
            recyler_view.visibility = VISIBLE
            no_result.visibility = GONE
            setupRecyclerView(itemList)
            runLayoutAnimation(recyler_view)

        }
    }

    override fun showProgressBar(show: Boolean) {
        progressBar?.visibility = View.GONE
    }

    override fun handleDatabaseLoadError(message: String?) {
        Toast.makeText(baseContext, message, LENGTH_LONG).show()
    }

    override fun showEmptyState() {
        recyler_view.visibility = GONE
        no_result.visibility = VISIBLE

    }

    private fun setupRecyclerView(itemList: ArrayList<ExpressEntryModel>) {
        expressEntryAdapter = ExpressEntryAdapter(this, twoPane, itemList)
        val dividerItemDecoration = DividerItemDecoration(recyler_view.context, LinearLayout.VERTICAL)
        recyler_view.addItemDecoration(dividerItemDecoration)

        val linearLayoutManager = LinearLayoutManager(baseContext);
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyler_view.layoutManager = linearLayoutManager
        recyler_view.adapter = expressEntryAdapter

    }

    private fun runLayoutAnimation(recyclerView: RecyclerView) {
        val context = recyclerView.context
        val loadLayoutAnimation =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_fall_down)

        recyclerView.layoutAnimation = loadLayoutAnimation
        recyclerView.adapter.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
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

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.d("ExpressEntryActivity", "onConnectionFailed:" + connectionResult.errorMessage)
        Toast.makeText(this, connectionResult.errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun setUpFilters() {
        if (year_2018.isChecked) {
            expressEntryPresenter.start()
        }

        year_2018.setOnClickListener {
            if (year_2018.isChecked) {
                expressEntryPresenter.showDataFor("2018")
            }
        }

        year_2017.setOnClickListener {
            if (year_2017.isChecked) {
                expressEntryPresenter.showDataFor("2017")
            }
        }

        year_2016.setOnClickListener {
            if (year_2016.isChecked) {
                expressEntryPresenter.showDataFor("2016")

            }
        }

        year_2015.setOnClickListener {
            if (year_2015.isChecked) {
                expressEntryPresenter.showDataFor("2015")
            }
        }
    }

}